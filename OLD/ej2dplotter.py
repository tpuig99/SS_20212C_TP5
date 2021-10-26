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

    data = np.genfromtxt(".\TP4\datos7Jupiter.csv", delimiter=",", names=["spaceshipVel", "marsMinDist", "arrivalTime"])
            
    plt.plot(data['spaceshipVel'],(data['arrivalTime']/(60*60*24))-655,'r.')
    plt.grid(b=True, which='both', axis='both')
    plt.xlabel('Velocidad de salida de la neve (Km/s)')
    plt.ylabel('Dias de viaje (Dias)')
    plt.show()
