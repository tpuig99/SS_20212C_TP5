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
	group = ['1.2','1.8','2.4','3.0']
	data1 = [3.07, 4.43, 5.58, 6.91]
	errors = [0.46, 0.65, 0.66, 0.71]

	plt.errorbar(group, data1, yerr=errors, fmt='-o')
	plt.grid(b=True, which='both', axis='both')
	plt.ylabel('Caudal Estacionario Promedio (particulas/s)')
	plt.xlabel('Tamaño de Apertura (m)')
	plt.show()

	return

if __name__ == "__main__":
	plotFlow()