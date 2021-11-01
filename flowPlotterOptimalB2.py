import sys,time,json,math,random
import matplotlib.pyplot as plt
import numpy as np

# Example Run
# python .\Analysis\plotChart1.py .\GraphData\chart_paper_1.json

data = -1
outFileName = -1

def calculateOptimalB():
	B = 1.475
	group = [1.2,1.8,2.4,3.0]
	data1 = [3.07, 4.43, 5.58, 6.91]
	errors = [0.46, 0.65, 0.66, 0.71]

	x = []
	for i in np.arange(1.2,3,0.01):
		x.append(i)

	y = []
	for j in range(0,len(x)):
		y.append(B * math.pow(x[j],3/2))
	
	plt.errorbar(group, data1, yerr=errors, fmt='-o')
	plt.plot(x, y, 'r.-')
	plt.grid(b=True, which='both', axis='both')
	plt.ylabel('Caudal Estacionario Promedio (particulas/s)')
	plt.xlabel('Tamaño de apertura (m)')
	plt.show()

	return

if __name__ == "__main__":
	calculateOptimalB()