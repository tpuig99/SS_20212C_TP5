import sys
import matplotlib.pyplot as plt


if __name__ == '__main__':

    i = 0
    dts = []
    gear_ecm = []
    beeman_ecm = []
    verlet_ecm = []


    with open("out_errors.txt") as f:
        
        for line in f:
            value = i % 4
            if value == 0:
                dts.append(float(line))
            elif value == 1:
                gear_ecm.append(float(line))
            elif value == 2:
                beeman_ecm.append(float(line))
            else:
                verlet_ecm.append(float(line))
            i += 1

    formats = [ 'g.-', 'b.-', 'k.-']
    labels = ['Beeman ECM', 'Gear Predictor-Corrector Order 5 ECM', 'Verlet ECM']
    
    plt.plot(dts, beeman_ecm, formats[0], label=labels[0])
    plt.plot(dts, gear_ecm, formats[1], label=labels[1])
    plt.plot(dts, verlet_ecm, formats[2], label=labels[2])

    plt.grid(b=True, which='both', axis='both')
    plt.ylabel('ECM')
    plt.xlabel('Diferencial de Tiempo (s)')
    plt.yscale('log')
    plt.xscale('log')
    plt.legend()
    plt.show()
