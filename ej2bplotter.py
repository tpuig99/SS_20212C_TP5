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

    data = np.genfromtxt(".\TP4\datos3Jupiter.csv", delimiter=",", names=["spaceshipVelocity", "t"])
            
    plt.plot(data['t']-655,data['spaceshipVelocity'],'r.-')
    plt.grid(b=True, which='both', axis='both')
    plt.ylabel('Velocidad de la nave (Km/s)')
    plt.xlabel('Dias desde despegue (Dias)')
    plt.show()
