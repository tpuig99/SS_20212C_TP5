import sys
import matplotlib.pyplot as plt


if __name__ == '__main__':

    if (len(sys.argv) < 2):
        print("Wrong amount of parameters")
        exit()

    times = []
    positions = []
    velocities = []

    with open(sys.argv[1]) as f:
        even = True
        for line in f:
            if even:
                times.append(float(line))
            else:
                aux = line.replace(',', '.')
                values = aux.split()
                positions.append(float(values[0]))
                velocities.append(float(values[1]))
            even = not even
            
    plt.plot(times, positions,'r.-')
    plt.grid(b=True, which='both', axis='both')
    plt.ylabel('Posición')
    plt.xlabel('Tiempo (s)')
    plt.show()
