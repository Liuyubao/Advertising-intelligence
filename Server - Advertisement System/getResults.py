import argparse,logging
import numpy as np
import mxnet as mx
import cv2, dlib
from lightened_moon import lightened_moon_feature
import pdb

text = ["5_o_Clock_Shadow","Arched_Eyebrows","Attractive","Bags_Under_Eyes","Bald", "Bangs","Big_Lips","Big_Nose",
            "Black_Hair","Blond_Hair","Blurry","Brown_Hair","Bushy_Eyebrows","Chubby","Double_Chin","Eyeglasses","Goatee",
            "Gray_Hair", "Heavy_Makeup","High_Cheekbones","Male","Mouth_Slightly_Open","Mustache","Narrow_Eyes","No_Beard",
            "Oval_Face","Pale_Skin","Pointy_Nose","Receding_Hairline","Rosy_Cheeks","Sideburns","Smiling","Straight_Hair",
            "Wavy_Hair","Wearing_Earrings","Wearing_Hat","Wearing_Lipstick","Wearing_Necklace","Wearing_Necktie","Young"]


text2 = ["Young","Male","Chubby","Eyeglasses","Heavy_Makeup","Smiling"]
attrIndexs=[39, 20, 13, 15, 18, 31]

# input the img and output the result



def analyzeImg(img):


    allFaces=[] # return values
    symbol = lightened_moon_feature(num_classes=40, use_fuse=True)
    devs = mx.cpu()
    _, arg_params, aux_params = mx.model.load_checkpoint('./model/lightened_moon/lightened_moon_fuse', 82)
    
    #use sth in dlib and opencv
    detector = dlib.get_frontal_face_detector()
    face_cascade = cv2.CascadeClassifier('./model/opencv/cascade.xml')

    # read img
    faces=detector(img, 1)

    gray = np.zeros(img.shape[0:2])
    if len(faces) == 0:
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        opencv_faces = face_cascade.detectMultiScale(gray, 1.3, 5)
        for (x,y,w,h) in opencv_faces:
            faces.append(dlib.rectangle(int(x), int(y), int(x+w), int(y+h)))

    #print "****number of the faces: ****"+str(len(faces))
    if len(faces) == 0:
        print "No faces detected"
    else:
        print "Amount of faces recognised: %d" % (len(faces))


    for f in faces:
        # draw all the rects of the faces
        cv2.rectangle(img, (f.left(), f.top()), (f.right(), f.bottom()), (0,0,255), 2)

    # #save the pictures with rects
    # cv2.imwrite(imgStr.replace('jpg', 'png'), img)

    num=1
    for face in faces:
        try:
            # crop face area
            gray2 = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
            pad = [0.25, 0.25, 0.25, 0.25] 
            left = int(max(0, face.left() - face.width()*float(pad[0])))
            top = int(max(0, face.top() - face.height()*float(pad[1])))
            right = int(min(gray2.shape[1], face.right() + face.width()*float(pad[2])))
            bottom = int(min(gray2.shape[0], face.bottom()+face.height()*float(pad[3])))
            gray2 = gray2[left:right, top:bottom]

            gray2 = cv2.resize(gray2, (128, 128))/255.0
            img2 = np.expand_dims(np.expand_dims(gray2, axis=0), axis=0)


            # get pred
            arg_params['data'] = mx.nd.array(img2, devs)
            exector = symbol.bind(devs, arg_params ,args_grad=None, grad_req="null", aux_states=aux_params)
            exector.forward(is_train=False)
            exector.outputs[0].wait_to_read()
            output = exector.outputs[0].asnumpy()
            pred = np.ones(40)
            
            tempFace=[]
            for i in attrIndexs:
                if output[0][i] < 0:
                    tempFace.append(False)
                else:
                    tempFace.append(True)
            allFaces.append(tempFace)

        except BaseException:
            print "skip"
    return allFaces

def analyseRawImageBytes(rawImage):
    nparr = np.fromstring(rawImage, np.uint8)
    img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
    return analyzeImg(img)





