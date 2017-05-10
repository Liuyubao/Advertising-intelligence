import sys
import Database
import getResults

from twisted.python import log
from OpenSSL import SSL
from twisted.internet import ssl, reactor, protocol
from twisted.internet.protocol import Protocol
from twisted.protocols import basic



class Echo(Protocol):
    def connectionMade(self):
        print "Connection Made"
        self.transport.write("Welcome! Please send me a message and I will send it back to you! :)")
    
    
    def dataReceived(self, data):
        print "Data received!"
        print data
        self.transport.write(data)
    
class ImageReceiveProtocol(basic.LineReceiver):
    delimiter = '\n'
    savemode=False
    db = Database.Database()
    
         
    def connectionMade(self):                       
        self.imageBytes = ''
        self.advertisementID = 0
        print "Connection Made"
                
                
    def connectionLost(self, reason):
        self.imageBytes = ''
        self.advertisementID = 0
        print "Connection Closed"
        print reason
        
        

    def lineReceived(self, line):
        #receive an adverisementID
        print "\nReceiving a new message from the client"
        line = line.strip()
        self.advertisementID = int(line)
        print "Advertisement ID: %d" % (self.advertisementID) 
        self.setRawMode()

                    
    def rawDataReceived(self, data): 
        #receive an image      
        #print('Receiving file chunk (%d Bytes)' % (len(data)))
        if data.endswith('\r\n'):
            # Last chunk
            data = data[:-2]
            self.imageBytes += data

            print "Received the entire image." 
            if(self.savemode):
                self.saveImage()
            else:
                self.handleImage()         
                
            
            #remove the image and go back to receiving an advertisementID
            self.imageBytes = ''
            self.setLineMode()
            
        else:
            self.imageBytes += data


    def saveImage(self):
        f = open("/home/terry/img%d.jpg" % self.advertisementID, 'wb')
        f.write(self.imageBytes)
        f.close()
    

            
    def handleImage(self):
    print "\nAnalyzing image..."
        faceData = getResults.analyseRawImageBytes(self.imageBytes)
        recommendation = 0
        for f in faceData:
            age = 0 if (f[0] == True) else 1
            gender = 'm' if (f[1] == True) else 'f'
            chubby = f[2]
            glasses = f[3]
            makeup = f[4]
            smiling = f[5]
            print "Recognised face has the following attributes:\nAge:\t%d\nGender:\t%s\nChubby:\t%r\nGlasses:\t%r\nMake-up:\t%r\nSmiling:\t%r" % (age, gender, chubby, glasses, makeup, smiling)
            print "submitting result to the database"
            self.db.submitMeasurement(age, gender, chubby, glasses, makeup, smiling, self.advertisementID)
            recommendation = self.db.getReccomendationBasedOnGender(gender)
            print "sending back a recommendation for the client: '%d'" % (recommendation)
        self.transport.write(str(recommendation))
         


def verifyCallback(connection, x509, errnum, errdepth, ok):
    if not ok:
        print 'invalid cert from subject:', x509.get_subject()
        return False
    else:
        print "Certificates are fine"
    return True

            
if __name__ == '__main__':
    log.startLogging(sys.stdout)
    factory = protocol.ServerFactory()
    factory.protocol = ImageReceiveProtocol
    myContextFactory = ssl.DefaultOpenSSLContextFactory('frserver.key', 'frserver.cert')

    ctx = myContextFactory.getContext()
    ctx.set_verify(SSL.VERIFY_PEER | SSL.VERIFY_FAIL_IF_NO_PEER_CERT, verifyCallback)

    # Since we have self-signed certs we have to explicitly
    # tell the server to trust them.
    ctx.load_verify_locations("frclient.cert")

    reactor.listenSSL(8065, factory, myContextFactory)
    print "starting server..."
    reactor.run()