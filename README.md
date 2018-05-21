# AppDynamics Monitoring Extension for PCF Firehose

### Use Case
Pivotal Cloud Foundry (PCF) is a multi cloud application platform as a service (PaaS). It is promoted for continuous delivery as it supports the full application development lifecycle, from initial development through all testing stages to deployment. Cloud Foundry’s container-based architecture runs apps in any programming language over a variety of cloud service providers.

The AppDynamics Monitoring Extension for PCF Firehose provides metrics generated by the Loggregator Firehose about the usage of its features and corresponding resources using a Nozzle. 

### Prerequisites 

1. In order to use this extension, you do need a [Standalone JAVA Machine Agent](https://docs.appdynamics.com/display/PRO44/Java+Agent) or [SIM Agent](https://docs.appdynamics.com/display/PRO44/Server+Visibility). 
For more details on downloading these products, please visit https://download.appdynamics.com/.

2. The extension needs to be able to connect to the Loggregator Nozzle in order to be able to collect and send the metrics. 
To do this, you will have to either establish a remote connection in between the extension and the product, or have an agent on the same machine running the product in order for the extension to collect and send the metrics.

The PCF Firehose Monitoring extension connects to the Loggregator Nozzle using gRPC over HTTP2. To obtain the BOSH endpoint for the Nozzle, please follow these steps: 
  a. Log into your PCF Ops Manager instance and click on the PAS (Pivotal Applications Service) tile
  b. Click on the status tab, and get the IP address for Loggregator Trafficcontroller. The port is conventionally 8082. 
 

3. In order to initiate this connection over a Netty client, some certificates are needed. The root CA certificate can be downloaded from the Pivotal Ops Manager instance specific to your PCF environment. The connection requires an additional certificate and a private key, which can be obtained using the OM tool (https://github.com/pivotal-cf/om). 

4. Once you've downloaded the OM tool, please run the following command to generate the aforementioned certificate and private key: 
```
./om-darwin -t <YOUR_OPS_MANAGER_URL> -k -u <USERNAME> -p <PASSWORD> curl -x POST -p /api/v0/certificates/generate -d '{ "domains": ["*.example.com", "*.sub.example.com"] }'
```
5. Please save the certificate and key to separate files ensuring that there is no whitespace between the code. The certificate & root CA certificate need to be converted to a x509 compatible PEM format, which can be done by running the following command: 
```openssl x509 -in <PATH_TO_CERTIFICATE> -out <PATH/cert.pem> -outform PEM```

6. The Netty Client only supports PKCS8 private keys, and the key obtained in step (4) needs to be converted as such. This can be done using the following command: 
```openssl pkcs8 -topk8 -nocrypt -in <PATH_TO_KEY> -out <NAME>.key```


### Installation 
<Insert information about the tile, adding environment variables etc here>  

### Configuration 
<Insert information about the tile, adding environment variables etc here>  

### Metrics 
The extension publishes all the Key Performance Indicators (KPIs) for monitoring Pivotal Cloud Foundry. These include: 
1. Diego Auctioneer Metrics
2. Diego BBS Metrics 
3. Diego Cell Metrics 
4. Diego Locket Metrics 
4. Diego Route Emitter Metrics 
5. PAS MySQL KPIs 
6. Gorouter Metrics 
7. UAA Metrics 
8. Firehose Metrics
9. System (BOSH) Metrics

The extension also published some Key Capacity Scaling Indicators, which include: 
1. Diego Cell Capacity Scaling Indicators
2. Firehose Performance Scaling Indicators
3. CF Syslog Drain Performance Scaling Indicators
4. Router Performance Scaling Indicators 
5. UAA Performance Scaling Indicators

Detailed information about the purpose and scale of the aforementioned metrics can be found in the Pivotal documentation: 
https://docs.pivotal.io/pivotalcf/2-0/monitoring/kpi.html
https://docs.pivotal.io/pivotalcf/2-1/monitoring/key-cap-scaling.html

### Troubleshooting
Please follow the steps listed in the [extensions troubleshooting document](https://community.appdynamics.com/t5/Knowledge-Base/How-to-troubleshoot-missing-custom-metrics-or-extensions-metrics/ta-p/28695) in order to troubleshoot your issue. These are a set of common issues that customers might have faced during the installation of the extension. If these don't solve your issue, please follow the last step on the troubleshooting-document to contact the support team.

## Version
|          Name            |  Version   |
|--------------------------|------------|
|Extension Version         |1.0.0       |
|Controller Compatibility  |3.7 or Later|
|Product Tested On         |Loggregator 2.x on a full PCF deployment (PIE-20)|
|Last Update               |05/21/2018 |
|List of Changes           |[Change log](https://github.com/Appdynamics/netscaler-monitoring-extension/blob/netscaler-monitoring-extension-1.0.0/changelog.md) |
