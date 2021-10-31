import sys,time,json,math,random
import matplotlib.pyplot as plt
import numpy as np

# Example Run
# python .\Analysis\plotChart1.py .\GraphData\chart_paper_1.json

data = -1
fileName = -1

def parseArgs():
	global data
	global fileName
	n = len(sys.argv)
	if n < 2:
		print("Arguments should be:\n *P:path to the simulation")
		exit ()
	
	# fileName = ".\TP5\sim1_light.json"
	fileName = sys.argv[1];
	
def plotFlow():
	data = np.genfromtxt(fileName, delimiter=",", names=["t", "deltaN", "acumN"])

	plt.plot(data['t'], data['acumN'],'r.-')
	plt.grid(b=True, which='both', axis='both')
	plt.ylabel('Distancia a Jupiter (Km)')
	plt.xlabel('Dia de despegue (Dias)')
	plt.show()

	return

if __name__ == "__main__":
	parseArgs()
	plotFlow()