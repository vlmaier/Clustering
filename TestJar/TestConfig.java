import vm.VmEnvironment;
import vm.vagrant.configuration.VagrantEnvironmentConfig;
import vm.vagrant.configuration.VagrantVmConfig;
import vm.vagrant.configuration.builder.*;
import vm.vagrant.util.Protocol;

public class TestConfig implements VmEnvironment {

    public VagrantEnvironmentConfig createEnvironment() {
        VagrantVmConfig vmConfig1 = VagrantVmConfigBuilder
                .create()
                .withName("Test-VM")
                .withHostName("Cluster.TestVM")
                .withBoxName("minimal/xenial64")
                .withVagrantNetworkConfig(VagrantNetworkConfigBuilder
                        .createPortForwardingConfig()
                        .withName("Test-PortForwarding")
                        .withAutoCorrect(true)
                        .withGuestPort(80)
                        .withHostIp("127.0.0.1")
                        .withHostPort(1337)
                        .withProtocol(Protocol.TCP)
                        .withService("http")
                        .build())
                .withBootTimeout(120)
                .withCommunicator("ssh")
                .withPostUpMessage("Provisioning of Test-VM complete!")
                .withProvider(VagrantProviderConfigBuilder
                        .create()
                        .withName("virtualbox")
                        .withGuiMode(false)
                        .withMemory(512)
                        .withCpus(2)
                        .withVmName("Test-VM")
                        .build())
                .withVagrantProvisionerConfig(new VagrantProvisionerConfigBuilder().createShellConfig()
                        .withInline(
                                "apt-get -y update &&" +
                                        "apt-get -y install apache2 && " +
                                        "echo 'Hi from your local Test-VM!' > /var/www/html/index.html && " +
                                        "systemctl start apache2")
                        .build())
                .build();
        VagrantEnvironmentConfig environmentConfig = VagrantEnvironmentConfigBuilder
                .create()
                .withVagrantVmConfig(vmConfig1)
                .build();
        environmentConfig.setVersion("test_version");
        return environmentConfig;
    }
}
