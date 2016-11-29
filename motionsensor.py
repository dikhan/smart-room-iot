import RPi.GPIO as GPIO
import time
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)

LED_PIN=3
GPIO.setup(LED_PIN,GPIO.OUT)     #Define pin 3 as an output pin

PIR_SENSOR_PIN=7
GPIO.setup(PIR_SENSOR_PIN, GPIO.IN)

TIME_TO_SLEEP=1

while True:
       i=GPIO.input(PIR_SENSOR_PIN)
       time.sleep(TIME_TO_SLEEP)
       if i==0:                 #When output from motion sensor is LOW
             print "No intruders",i
             GPIO.output(LED_PIN, 0)  #Turn OFF LED
             #time.sleep(0.1)
       elif i==1:               #When output from motion sensor is HIGH
             print "Intruder detected",i
             GPIO.output(LED_PIN, 1)  #Turn ON LED
             

