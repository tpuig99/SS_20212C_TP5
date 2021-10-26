import sys
import matplotlib.pyplot as plt
import numpy as np

if __name__ == '__main__':

    if (len(sys.argv) < 2):
        print("Wrong amount of parameters")
        exit()

    times = []
    positions = []
    velocities = []

    data = np.genfromtxt(".\TP4\datos2Jupiter.csv", delimiter=",", names=["lounchPctg", "spaceshipMarsMinDist", "spaceshipTimeOfArrival", "t_f", "lounchDate"])
            
    plt.plot(data['lounchDate'], data['spaceshipMarsMinDist'],'r.-')
    plt.grid(b=True, which='both', axis='both')
    plt.ylabel('Distancia a Jupiter (Km)')
    plt.xlabel('Dia de despegue (Dias)')
    plt.show()
