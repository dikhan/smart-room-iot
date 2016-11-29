import RPi.GPIO as GPIO
import time
import socket

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)

PIR_SENSOR_PIN=7
GPIO.setup(PIR_SENSOR_PIN, GPIO.IN)

# subscriber
host=localhost
port=5005

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
          
def notify_subscriber(p):
  sock = socket.socket(socket.AF_NET, socket.SOCK_DGRAM)
  sock.sendto(p, (host, port) )
