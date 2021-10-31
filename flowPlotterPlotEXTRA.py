import sys,time,json,math,random
import matplotlib.pyplot as plt
import numpy as np
import statistics

# Example Run
# python .\Analysis\plotChart1.py .\GraphData\chart_paper_1.json

data = -1
fileName1 = -1
fileName2 = -1
fileName3 = -1
	
def plotFlow():
	group = ['d=1.2,N=200','d=1.8,N=260','d=2.4,N=320','d=3.0,N=380']
	data1 = [3.07, 4.43, 5.58, 6.91]
	errors = [0.46, 0.65, 0.66, 0.71]

	plt.errorbar(group, data1, yerr=errors, fmt='-o')
	plt.grid(b=True, which='both', axis='both')
	plt.ylabel('Caudal Estacionario Promedio (particulas/s)')
	plt.xlabel('Caso')
	plt.show()

	return

if __name__ == "__main__":
	plotFlow()