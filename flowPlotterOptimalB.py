import sys,time,json,math,random
import matplotlib.pyplot as plt
import numpy as np

# Example Run
# python .\Analysis\plotChart1.py .\GraphData\chart_paper_1.json

data = -1
outFileName = -1

def calculateOptimalB():
	Qs = [3.07, 4.43, 5.58, 6.91]
	ds = [1.2, 1.8, 2.4, 3.0]
	Bs = []
	errors = []

	min_B = -1
	min_error = 100000

	for B in np.arange(-7, 10, 0.001):
		error = 0
		for x in range(0, 4):
			experimentalValue = Qs[x]
			teoricalValue = B * math.pow(ds[x],3/2)
			error += math.pow((experimentalValue-teoricalValue),2)
		Bs.append(B)
		errors.append(error)
		if(error < min_error):
			min_error = error
			min_B = B

	print(min_B, min_error)
	plt.plot(Bs, errors,'r.-')
	plt.grid(b=True, which='both', axis='both')
	plt.ylabel('Error')
	plt.xlabel('B')
	plt.show()

	return

if __name__ == "__main__":
	calculateOptimalB()