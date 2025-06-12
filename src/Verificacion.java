import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Verificacion {

    static class Subnet {
        String network;
        String mask;
        int prefixLength;
        String firstIp;
        String lastIp;
        String broadcast;
        int totalHosts;

        public Subnet(String network, String mask, int prefixLength, String firstIp, String lastIp, String broadcast, int totalHosts) {
            this.network = network;
            this.mask = mask;
            this.prefixLength = prefixLength;
            this.firstIp = firstIp;
            this.lastIp = lastIp;
            this.broadcast = broadcast;
            this.totalHosts = totalHosts;
        }
    }

    public static List<Subnet> calculateVLSM(String networkIp, List<Integer> hostRequirements) throws UnknownHostException {
        Collections.sort(hostRequirements, Collections.reverseOrder());

        String[] networkParts = networkIp.split("/");
        InetAddress mainNetwork = InetAddress.getByName(networkParts[0]);
        int prefixLength = Integer.parseInt(networkParts[1]);

        int availableHosts = (int) Math.pow(2, 32 - prefixLength) - 2;
        int totalRequiredHosts = 0;

        for (int hosts : hostRequirements) {
            totalRequiredHosts += Math.pow(2, Math.ceil(Math.log(hosts + 2) / Math.log(2)));
        }

        if (totalRequiredHosts > availableHosts) {
            System.out.println("Error: Las subredes requieren " + totalRequiredHosts + " direcciones, " +
                    "pero la red principal solo tiene " + availableHosts + " direcciones disponibles.");
            return null;
        }

        List<Subnet> subnets = new ArrayList<>();
        int currentIp = ipToInt(mainNetwork);

        for (int hosts : hostRequirements) {
            int bitsNeeded = (int) Math.ceil(Math.log(hosts + 2) / Math.log(2));
            int subnetMask = 32 - bitsNeeded;

            int subnetSize = (int) Math.pow(2, bitsNeeded);
            InetAddress newSubnetNetwork = intToIp(currentIp);
            InetAddress newSubnetBroadcast = intToIp(currentIp + subnetSize - 1);

            subnets.add(new Subnet(
                    newSubnetNetwork.getHostAddress(),
                    prefixToMask(subnetMask),
                    subnetMask,
                    intToIp(currentIp + 1).getHostAddress(),
                    intToIp(currentIp + subnetSize - 2).getHostAddress(),
                    newSubnetBroadcast.getHostAddress(),
                    hosts
            ));

            currentIp += subnetSize;

            if (currentIp > ipToInt(intToIp(ipToInt(mainNetwork) + (int) Math.pow(2, 32 - prefixLength) - 1))) {
                System.out.println("Error: No hay suficiente espacio en la red para todas las subredes requeridas.");
                return null;
            }
        }

        return subnets;
    }

    public static void main(String[] args) throws UnknownHostException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduce la direccion IP de la red principal (por ejemplo, 192.168.1.0/24): ");
        String networkIp = scanner.nextLine();

        System.out.print("Introduce el numero de departamentos: ");
        int numDepartments = scanner.nextInt();

        List<Integer> hostRequirements = new ArrayList<>();

        for (int i = 0; i < numDepartments; i++) {
            System.out.print("Introduce la cantidad de dispositivos en el departamento " + (i + 1) + ": ");
            int hosts = scanner.nextInt();
            hostRequirements.add(hosts);
        }

        List<Subnet> vlsmSubnets = calculateVLSM(networkIp, hostRequirements);

        if (vlsmSubnets != null) {
            for (int i = 0; i < vlsmSubnets.size(); i++) {
                Subnet subnet = vlsmSubnets.get(i);
                System.out.println("Departamento " + (i + 1) + ":");
                System.out.println("  Subred: " + subnet.network + "/" + subnet.prefixLength);
                System.out.println("  Mascara de subred: " + subnet.mask);
                System.out.println("  Rango de IPs: " + subnet.firstIp + " - " + subnet.lastIp);
                System.out.println("  Direccion de Broadcast: " + subnet.broadcast);
                System.out.println("  Hosts soportados: " + subnet.totalHosts);
                System.out.println("  Bits de la máscara: " + subnet.prefixLength);
                System.out.println();
            }
        }

        scanner.close();
    }

    private static int ipToInt(InetAddress ip) {
        byte[] octets = ip.getAddress();
        int result = 0;
        for (byte octet : octets) {
            result = result << 8 | (octet & 0xFF);
        }
        return result;
    }

    private static InetAddress intToIp(int ip) throws UnknownHostException {
        return InetAddress.getByName(
                ((ip >> 24) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                (ip & 0xFF)
        );
    }

    private static String prefixToMask(int prefixLength) {
        int mask = 0xffffffff << (32 - prefixLength);
        return ((mask >> 24) & 0xff) + "." +
               ((mask >> 16) & 0xff) + "." +
               ((mask >> 8) & 0xff) + "." +
               (mask & 0xff);
    }
}
