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

def parseArgs():
	global data
	global fileName1
	global fileName2
	global fileName3
	
	# fileName = ".\TP5\sim1_light.json"
	# fileName1 = "./flowC_1.2_200_a.csv"
	# fileName2 = "./flowC_1.2_200_b.csv"
	# fileName3 = './flowC_1.2_200_c.csv'

	# fileName1 = "./flowC_1.8_260_a.csv"
	# fileName2 = "./flowC_1.8_260_b.csv"
	# fileName3 = './flowC_1.8_260_c.csv'

	# fileName1 = "./flowC_2.4_320_a.csv"
	# fileName2 = "./flowC_2.4_320_b.csv"
	# fileName3 = './flowC_2.4_320_c.csv'

	fileName1 = "./flowC_3.0_380_a.csv"
	fileName2 = "./flowC_3.0_380_b.csv"
	fileName3 = './flowC_3.0_380_c.csv'
	
def plotFlow():
	data1 = np.genfromtxt(fileName1, delimiter=",", names=["t", "deltaN", "acumN"])
	data2 = np.genfromtxt(fileName2, delimiter=",", names=["t", "deltaN", "acumN"])
	data3 = np.genfromtxt(fileName3, delimiter=",", names=["t", "deltaN", "acumN"])

	min = 0
	max = len(data1) - 2

	dataAVG = []
	dataSTD = []
	dataT = []
	for x in range(min, max):
		avg = (data1['deltaN'][x] + data2['deltaN'][x] + data3['deltaN'][x])/3
		std = np.std([data1['deltaN'][x], data2['deltaN'][x], data3['deltaN'][x]])
		dataAVG.append(avg)
		dataSTD.append(std)
		dataT.append(data1['t'][x])

	values = []
	for x in range(5, 55):
		values.append(dataAVG[x])

	avg = "{:.2f}".format(sum(values)/len(values))
	std = "{:.2f}".format(np.std(values))
	print(avg,std)

	plt.errorbar(dataT, dataAVG, yerr=dataSTD, fmt='-o')
	plt.grid(b=True, which='both', axis='both')
	plt.ylabel('Promedio del Caudal (particulas/s)')
	plt.xlabel('Tiempo (s)')
	plt.show()

	return

if __name__ == "__main__":
	parseArgs()
	plotFlow()