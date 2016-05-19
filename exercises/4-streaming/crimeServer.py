#!/usr/bin/env python

import time
import socket
import signal
import sys

DELAY=0.5
PORT=9999

serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

def exit_gracefully(signum,frame):
    serversocket.close()

signal.signal(signal.SIGINT, exit_gracefully)
signal.signal(signal.SIGTERM, exit_gracefully)

host = "localhost"
port = PORT
print (host)
print (port)
serversocket.bind((host, port))
serversocket.listen(5)

(clientsocket, address) = serversocket.accept()
print("Connection found")

with open("SacramentoCrimes.csv") as f:
	for line in f:
		print (line)
		clientsocket.send(line.encode())
		print ("sent")
		time.sleep(DELAY)


serversocket.close()
