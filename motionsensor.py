import RPi.GPIO as GPIO
import time
from socket import  *
from sys import *

def notify_subscriber(p):
  #sock = socket.socket(socket.AF_NET, socket.SOCK_DGRAM)
  sock = socket(AF_INET, SOCK_DGRAM)
  sock.sendto(p, (host, port) )

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)

PIR_SENSOR_PIN=7
GPIO.setup(PIR_SENSOR_PIN, GPIO.IN)

# subscriber
if len(argv) > 1:
  host = argv[1]
else:
  host="192.168.1.100"
print host
port=5006

TIME_TO_SLEEP=1
while True:
  i = GPIO.input(PIR_SENSOR_PIN)
  time.sleep(TIME_TO_SLEEP)

  if i==0:   #When output from motion sensor is LOW
    print "No intruders"
    notify_subscriber("no motion detected")
     
   
  elif i==1: #When output from motion sensor is HIGH
    print "Intruder detected"
    notify_subscriber("motion detected")
          

