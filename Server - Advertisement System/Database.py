import MySQLdb
class Database(object):



    def __init__(self):
        self.db = MySQLdb.connect("172.16.19.252", "MeasurementSubmit", "1234", "sadb")
        self.cursor = self.db.cursor()
        print "Database connection Established"
    
    def submitMeasurement(self, age, gender, chubby, glasses, makeup, smiling, advertisementID):
        sql = "INSERT INTO Measurements \
            (Age, Gender, Chubby, Glasses, Makeup, Smiling, AdvertisementID) \
            VALUES ('%d', '%s', '%i', '%i', '%i', '%i', '%d' )" % \
            (age, gender, chubby, glasses, makeup, smiling, advertisementID)
        try:
            # Execute the SQL command
            self.cursor.execute(sql)
            # Commit your changes in the database
            self.db.commit()
        except Exception, e:
            print repr(e)
            # Rollback in case there is any error
            self.db.rollback()   
    
    def getReccomendationBasedOnGender(self, gender):
        sql = "SELECT AdvertisementID, COUNT(AdvertisementID) AS 'count' \
            FROM Measurements \
            WHERE Gender = '%s' \
            GROUP BY AdvertisementID \
            ORDER BY `count` DESC \
            LIMIT 1" % (gender)
        try:
            self.cursor.execute(sql)
            result = self.cursor.fetchone()
            return result[0]
        except:
            print "Error: unable to fetch data"
        
    def __del__(self):
        try:
            self.db.close()
            print "Database connection Closed"
        except AttributeError, e:
            print "Database connection doesn't exist"

            
if __name__ == "__main__":
    print "started"
    db = Database()
    result = db.getReccomendationBasedOnGender('m')
    print result
    """
    while result is not 4:
        db.submitMeasurement(0, 'm', False, False, False, True, 4)
        result = db.getReccomendationBasedOnGender('m')
        print result
    """    
    