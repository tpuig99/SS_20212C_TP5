import sys
import matplotlib.pyplot as plt


if __name__ == '__main__':

    if (len(sys.argv) < 2):
        print("Wrong amount of parameters")
        exit()

    dt = sys.argv[1]      

    tags = [f"out_analytic_{dt}.txt", f"out_beeman_{dt}.txt", f"out_gear_{dt}.txt", f"out_verlet_{dt}.txt"]  

    times = []
    positions = []
    velocities = []

    for i in range(len(tags)):
        times.append([])
        positions.append([])
        velocities.append([])

        with open(tags[i]) as f:
            even = True
            for line in f:
                if even:
                    times[i].append(float(line))
                else:
                    aux = line.replace(',', '.')
                    values = aux.split()
                    positions[i].append(float(values[0]))
                    velocities[i].append(float(values[1]))
                even = not even

    formats = [ 'r.-', 'g.-', 'b.-', 'k.-']
    labels = ['Analytic Solution', 'Beeman', 'Gear Predictor-Corrector Order 5', 'Verlet']
    
    for i in range(len(tags)):
        plt.plot(times[i], positions[i], formats[i], label=labels[i])

    plt.grid(b=True, which='both', axis='both')
    plt.ylabel('Posición (m)')
    plt.xlabel('Tiempo (s)')
    plt.legend()
    plt.show()
