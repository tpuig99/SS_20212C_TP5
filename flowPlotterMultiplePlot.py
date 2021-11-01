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

def plotAcum():
	global subcases
	subcases = ["a", "b", "c", "d"]
	
	errors = []
	x_axis = []
	y_axis = []

	for i in range(len(subcases)):

		data = np.genfromtxt(f"ej1a_flow_{subcases[i]}.csv", delimiter=",", names=["t", "deltaN", "acumN"])

		if i == 0:
			for x in range(int(data['acumN'][-1]) + 1):
				x_axis.append(x)
				y_axis.append([])

		Nprev = 0
		for x in range(len(data['acumN'])):
			
			Ncurr = int(data['acumN'][x])
			if Ncurr > Nprev:
				y_axis[Nprev].append(data['t'][x])
				Nprev = Ncurr			

	x = []
	y = []

	for i in range(len(y_axis)):
		if len(y_axis[i]) > 0:
			x.append(x_axis[i])
			y.append(np.mean(y_axis[i]))
			errors.append(np.std(y_axis[i]))

	# for i in range(len(y)):
	# 	if i > 0:
	# 		aux = (x[i] - x[i-1]) / (y[i] - y[i-1])
	# 		print('Q = ', aux)

	plt.plot(y, x, 'r.')
	plt.errorbar(y, x, xerr=errors, fmt=' ', color='black')
	plt.grid(b=True, which='both', axis='both')
	plt.ylabel('Cantidad de Partículas que Salieron')
	plt.xlabel('Tiempo (s)')
	plt.show()


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
	plotAcum()