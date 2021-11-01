import sys,time,json,math,random
import matplotlib.pyplot as plt
import numpy as np

# Example Run
# python .\Analysis\plotChart1.py .\GraphData\chart_paper_1.json

data = -1
outFileName = -1

def parseArgs():
	global data
	global outFileName
	n = len(sys.argv)
	if n < 3:
		print("Arguments should be:\n *P:path to the simulation")
		exit ()
	
	# fileName = ".\TP5\sim1_light.json"
	fileName = sys.argv[1];
	outFileName = sys.argv[2];
	with open(fileName) as f:
		data = json.load(f)
	
def parseFlow():
	events = data['events']
	Ly = data['Ly']
	lastCirclesOut = 0;
	ts = []
	deltaNs = []
	acumNs = []
	dt = 0.0375 # cambia cada cuanto se printea una nueva row

	iter = 0
	dt_deltaN = 0

	totalCirclesOut = 0
	for event in events:
		t = event['total_t']
		circles = event['circles']
		currCirclesOut = 0
		totalCirclesOut = 0
		for circle in circles:
			y = circle['y']
			if(y >= Ly/2):
				currCirclesOut += 1
				totalCirclesOut += 1
		flow = currCirclesOut - lastCirclesOut
		lastCirclesOut = currCirclesOut
		if((t - iter * dt) > dt):
			ts.append(iter*dt);
			deltaNs.append(dt_deltaN)
			acumNs.append(totalCirclesOut)
			iter+=1;
			dt_deltaN = flow
		else:
			dt_deltaN+=flow
	ts.append(iter*dt);
	deltaNs.append(dt_deltaN)
	acumNs.append(totalCirclesOut)

	a = np.array([ts,deltaNs,acumNs])
	fmt = '%.2f', '%.0f', '%.0f'
	np.savetxt(outFileName, np.transpose(a), delimiter=',', fmt=fmt, header="t,deltaN,acumN")

	return

if __name__ == "__main__":
	np.set_printoptions(formatter={'float': lambda x: "{0:0.3f}".format(x)})
	parseArgs()
	parseFlow()