import sys, math, json
from random import seed
from random import uniform
from random import randint
from datetime import datetime

seed(datetime.now().microsecond)

circles = []

N = -1		#ammount of particles
L = -1		#simulation space length
v = -1		#particle speed

rmin = -1
rmax = -1
fname = "conds"
COTA = 2000

class Circle:
	def __init__(self, x, y, r):
		self.x = x
		self.y = y
		self.r = r

def parseArgs():
	n = len(sys.argv)
	if n<8:
		print("Arguments should be 7")
		exit()

	global N
	N = int(sys.argv[1])
	global Lx
	Lx = float(sys.argv[2])
	global Ly
	Ly = float(sys.argv[3])
	global rmin
	rmin = float(sys.argv[4])
	global rmax
	rmax = float(sys.argv[5])
	global v
	v = float(sys.argv[6])
	global fname
	fname = sys.argv[7]
	
def collide(c1, c2):
	normal_dist = math.sqrt((c2.x - c1.x)**2 + (c2.y - c1.y)**2) - c1.r - c2.r
	if normal_dist <= 0:
		return True
	return False

def overlaps(circle):
	for other in circles:
		if(collide(other, circle)):
			return True
	return False

def respectsBoundaries(x, y, r):
	return ((x + r < Lx) and  (x - r > 0)) and ((y + r < Ly/2) and  (y - r > 0))

def generate():
	iter = 0
	while (len(circles) < N and iter < COTA):
		x = uniform(0, Lx)
		y = uniform(0, Ly/2)
		r = uniform(rmin, rmax)
		iter += 1
		if respectsBoundaries(x,y,r):
			circle = Circle(x, y, r)
			if not overlaps(circle):
				circles.append(circle)
				iter = 0

	if(iter == COTA):
		print("too many particles")
		exit()

	jsonStr = json.dumps({ 'N':N , 'Lx':Lx, 'Ly':Ly, 'rmin':rmin, 'rmax':rmax, 'v':v ,'circles':[circle.__dict__ for circle in circles]})
	f = open(f"{fname}.json", "w")
	f.write(jsonStr)
	f.close()

if __name__ == "__main__":
	#generate random data
	parseArgs()
	generate()
