# DeFAPSim

DeFAPSim is a simulator, which was developed with the aim to evaluate decentralized algorithms for solving the Fog Application Placement Problem (FAPP).
The implementation was done as part of the work "EdgeDecAp: An auction-based decentralized algorithm for optimizing application placement in edge computing" by Sven Smolka, Leon Wißenberg and Zoltán Ádám Mann. 
In this work, the EdgeDecAp algorithm was presented, which is based on the DecAp algorithm of Malek et al. [[1]](#1).
EdgeDecAp extends the DecAp algorithm to handle devices with different computing capacity and processing speed, as well as to account for the impact of end devices.
Also, some optimizations were made to further improve the performance of the algorithm.
The EdgeDecAp algorithm was implemented in DeFAPSim along with another decentralized state-of-the-art algorithm by Guerrero et al. [[2]](#2) (which we refer to as LDSPP) and a centralized cloud only placement algorithm. 
The following is a brief summary of the main features of the simulator.

## Infrastructure
In DeFAPSim, the infrastructure consists of cloud servers, edge nodes and end devices.
The simulator provides the possibility to characterize devices with a processing speed and set their capacities in terms of memory and computing power. 
Also, (directed and undirected) links, which are characterized by a latency, exist between devices.
Accordingly, the infrastructure can be modeled as a directed and undirected graph. 
A route solver is also implemented, which automatically determines the shortest paths between devices based on the latency of the links. 
Finally, the simulator also allows to define limited awareness between devices. 
Such limited awareness can be defined automatically for each device based on the number of network hops.

## Application
DeFAPSim uses applications which consist of several components. 
Components are characterized by their worst-case execution time and memory and computing power requirements.
The components are connected to each other by directed connectors, which represent the data flow. 
Connectors are also provided between components and end devices. 
Thus, the application structure can be modeled as a directed graph. 
Moreover, DeFAPSim offers the possibility to define dependencies and conflicts between components and location restrictions. 
Finally, the simulator also supports the migration of components to other devices.

## Metrics considered
The simulator allows to take into account the application latency, which is the sum of the data transfer times of the connectors and the execution times of the components. 

## Future contributions
The simulator can be extended by other authors and new algorithms can be added.
This can be done by authors submitting a pull request.<br/><br/><br/>

## References
<a id="1">[1]</a> 
S. Malek, M. Mikic-Rakic, N. Medvidovic, A decentralized redeployment algorithm for improving the availability of distributed systems, in: International Working Conference on Component Deployment, Springer, 2005, pp. 99–114.  
<a id="2">[2]</a> 
C. Guerrero, I. Lera, C. Juiz, A lightweight decentralized service placement policy for performance optimization in fog computing, Journal of Ambient Intelligence and Humanized Computing 10 (6) (2019) 2435–2452
