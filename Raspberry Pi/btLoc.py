from bluepy.btle import Scanner, DefaultDelegate, UUID
import pyrebase, json, datetime

#open the deviceConfig file to check for MAC address and for School
config = "none"
thisMAC = "none"
school_name = "none"
try:
        f = open('config.txt', 'r')
        thisMAC = f.readline().rstrip('\n')
        school_name = f.readline().rstrip('\n')
        f.close()
        
except:
        #get the WLAN mac address of this raspberry pi to know which beacon it is
        thisMAC = open('/sys/class/net/wlan0/address').read()
        
        print("The MAC address of this reciever is " + thisMAC)
        thisMAC = thisMAC.rstrip('\n')
        
        print("What is the name of the school this unit will be in?")
        school_name = input()

        with open('config.txt', 'w') as text_file:
                text_file.write(thisMAC+"\n")
                text_file.write(school_name)
                text_file.close()






class ScanDelegate(DefaultDelegate):
	def __init__(self):
		DefaultDelegate.__init__(self)

	def handleDiscovery(self, dev, isNewDev, isNewData):
		if isNewDev:
			print ("Calculating bracelet location for " + dev.addr)
		elif isNewData:
			print ("Received new data from " + dev.addr)


#First Auth with the database
config = {
        "apiKey": "AIzaSyDp38ZcH-fdSVvdWroIj4_zg9a54dd2wYM",
        "authDomain": "day-care-monitoring-system.firebaseapp.com",
        "databaseURL": "https://day-care-monitoring-system.firebaseio.com",
        "storageBucket": "day-care-monitoring-system.appspot.com",
        "serviceAccount": "serviceAccountKey.json"
}
firebase = pyrebase.initialize_app(config)

#get the database
db = firebase.database()
all_children = db.child("daycare").child(school_name).child('children').get()
childrenJson = json.dumps(all_children.val())
json_data = json.loads(childrenJson)

#create the list of mac address to compare to
set_of_macAddr = set()

#search through
for majorkey, subdict in json_data.items():
        set_of_macAddr.add(majorkey)
                        
#now that we can get the addresses we compare to what we see
#when we scan for them        

while True:
        #scanner = Scanner().withDelegate(ScanDelegate())
        scanner = Scanner()
        devices = scanner.scan(5.0)
        timestamp = str(datetime.datetime.now())
        db.child("daycare").child(school_name).child('children').child("aa:aa:aa:aa:aa").update({"timestamp": timestamp})
        for dev in devices:
                print(dev.addr)
                if dev.addr in set_of_macAddr:
                        distance = (dev.rssi * -1) / float(70)
                        if distance <= 1.05:
                                print("The bracelet " + dev.addr + " is located by this receiver.")
                                # put the bracelet location under the right school
                                location = db.child("daycare").child(school_name).child('children').child(dev.addr).update({"locationMAC": thisMAC})
                                timestamp = str(datetime.datetime.now())
                                db.child("daycare").child(school_name).child('children').child(dev.addr).update({"timestamp": timestamp})



	

