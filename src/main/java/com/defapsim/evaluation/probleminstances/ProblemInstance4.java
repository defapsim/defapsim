package com.defapsim.evaluation.probleminstances;

import com.defapsim.application.Application;
import com.defapsim.evaluation.Evaluation;
import com.defapsim.evaluation.problemInstancegenerator.ApplicationGenerator;
import com.defapsim.evaluation.problemInstancegenerator.InfrastructureGenerator;
import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.misc.xlscreator.XLSExporter;
import com.defapsim.policies.domain.DomainPolicy;
import com.defapsim.policies.domain.GlobalDomainPolicy;
import com.defapsim.policies.domain.HopDomainPolicy;
import com.defapsim.policies.initialplacement.InitialPlacementPolicy;
import com.defapsim.policies.initialplacement.RandomInitialPlacementPolicy;
import com.defapsim.simulations.CloudOnlySimulation;
import com.defapsim.simulations.EdgeDecApSimulation;
import com.defapsim.simulations.LDSPPSimulation;
import com.defapsim.simulations.Simulation;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.LinkedList;
import java.util.List;

public class ProblemInstance4 {

    public static void main(String[] args) {
        runProblemInstance4(1234, 1.F, new XSSFWorkbook(), 0);
    }

    public static void runProblemInstance4(Integer seed, Float withWorstCaseExcecutionTimeMultiplier, Workbook evaluation_workbook, Integer relativeDistance) {
        InfrastructureGenerator infrastructureGenerator = new InfrastructureGenerator()
                .withMinimumMemory(4.F).withMaximumMemory(16.F)
                .withMinimumComputingPower(2.F).withMaximumComputingPower(10.F)
                .withMinimumProcessingSpeed(1.9F).withMaximumProcessingSpeed(3.1F)
                .withMinimumCloudPoPLatency(30.F).withMaximumCloudPoPLatency(100.F)
                .withMinimumPoPPoPLatency(3.F).withMaximumPoPPoPLatency(7.F)
                .withMinimumBoxPoPLatency(1.F).withMaximumBoxPoPLatency(20.F)
                .withMinimumBoxEndDevicesLatency(1.F).withMaximumBoxEndDevicesLatency(2.F)
                .withSeed(seed).withEvaluationWorkbook(evaluation_workbook)
                .withRelativeDistance(relativeDistance);

        InitialPlacementPolicy initialPlacementPolicy = new RandomInitialPlacementPolicy().withSeed(seed);

        Infrastructure infrastructure = infrastructureGenerator.createPhase1(3,true);
        Application application = new ApplicationGenerator()
                .withWorstCaseExcecutionTimeMultiplier(withWorstCaseExcecutionTimeMultiplier)
                .withEvaluationWorkbook(evaluation_workbook)
                .withOldDevices(infrastructureGenerator.getOldDevices())
                .withRelativeX(relativeDistance)
                .withSeed(seed)
                .createApplication(infrastructure, 4);


        // SIMULATION OF THE CLOUD ONLY ALGORITHM
        Evaluation evaluationCloudOnly = new Evaluation().withAlgorithmName("CloudOnly");
        Simulation cloudOnlySimulation = new CloudOnlySimulation()
                .withEvaluation(evaluationCloudOnly)
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withInitialPlacementPolicy(initialPlacementPolicy);
        cloudOnlySimulation.prepareSimulation();
        cloudOnlySimulation.startSimulation();


        // SIMULATION OF THE LDSPP
        infrastructure.resetInfrastructure();
        Evaluation evaluationLDSPP = new Evaluation().withAlgorithmName("LDSPP");
        Simulation LDSPPSimulation = new LDSPPSimulation()
                .withEvaluation(evaluationLDSPP)
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withInitialPlacementPolicy(initialPlacementPolicy);
        LDSPPSimulation.prepareSimulation();
        LDSPPSimulation.startSimulation();


        // SIMULATION OF THE EdgeDecAp ALGORITHM 1 HOP DOMAIN
        DomainPolicy domainPolicy = new HopDomainPolicy().withHops(1);
        infrastructure.resetInfrastructure();
        Evaluation evaluationEdgeDecApAlgorithm1HopDomainPolicy = new Evaluation().withAlgorithmName("EdgeDecAp 1-Hop DP");
        Simulation EdgeDecApSimulation1 = new EdgeDecApSimulation()
                .isBeingDebugged(false)
                .withEvaluation(evaluationEdgeDecApAlgorithm1HopDomainPolicy)
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);
        EdgeDecApSimulation1.prepareSimulation();
        EdgeDecApSimulation1.startSimulation();

        // SIMULATION OF THE EdgeDecAp ALGORITHM 2 HOP DOMAIN
        domainPolicy = new HopDomainPolicy().withHops(2);
        infrastructure.resetInfrastructure();
        Evaluation evaluationEdgeDecApAlgorithm2HopDomainPolicy = new Evaluation().withAlgorithmName("EdgeDecAp 2-Hop DP");
        Simulation EdgeDecApSimulation2 = new EdgeDecApSimulation()
                .isBeingDebugged(false)
                .withEvaluation(evaluationEdgeDecApAlgorithm2HopDomainPolicy)
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);
        EdgeDecApSimulation2.prepareSimulation();
        EdgeDecApSimulation2.startSimulation();

        // SIMULATION OF THE EdgeDecAp ALGORITHM 3 HOP DOMAIN
        domainPolicy = new HopDomainPolicy().withHops(3);
        infrastructure.resetInfrastructure();
        Evaluation evaluationEdgeDecApAlgorithm3HopDomainPolicy = new Evaluation().withAlgorithmName("EdgeDecAp 3-Hop DP");
        Simulation EdgeDecApSimulation3 = new EdgeDecApSimulation()
                .isBeingDebugged(false)
                .withEvaluation(evaluationEdgeDecApAlgorithm3HopDomainPolicy)
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);
        EdgeDecApSimulation3.prepareSimulation();
        EdgeDecApSimulation3.startSimulation();

        // SIMULATION OF THE EdgeDecAp ALGORITHM GLOBAL DOMAIN
        domainPolicy = new GlobalDomainPolicy();
        infrastructure.resetInfrastructure();
        Evaluation evaluationEdgeDecApAlgorithmGlobalDomainPolicy = new Evaluation().withAlgorithmName("EdgeDecAp Global DP");
        Simulation EdgeDecApSimulationG = new EdgeDecApSimulation()
                .isBeingDebugged(false)
                .withEvaluation(evaluationEdgeDecApAlgorithmGlobalDomainPolicy)
                .withInfrastructure(infrastructure)
                .withApplication(application)
                .withDomainPolicy(domainPolicy)
                .withInitialPlacementPolicy(initialPlacementPolicy);
        EdgeDecApSimulationG.prepareSimulation();
        EdgeDecApSimulationG.startSimulation();

        List<Evaluation> evaluationList = new LinkedList<>() {{
            this.add(evaluationEdgeDecApAlgorithm1HopDomainPolicy);
            this.add(evaluationEdgeDecApAlgorithm2HopDomainPolicy);
            this.add(evaluationEdgeDecApAlgorithm3HopDomainPolicy);
            this.add(evaluationEdgeDecApAlgorithmGlobalDomainPolicy);
            this.add(evaluationLDSPP);
            this.add(evaluationCloudOnly);
        }};

        XLSExporter.getInstance().buildEvaluationXLSAlgorithmRow(evaluation_workbook, seed, 4, evaluationList, relativeDistance);

        System.out.println("Devices in infrastructure: " + infrastructure.getDevices().size());
        System.out.println("Total pops: " + (infrastructureGenerator.getPopID() - 1));
        System.out.println("Total homes: " + (infrastructureGenerator.getBoxID() - 1));
        System.out.println("Total edge nodes: " + ( infrastructureGenerator.getBoxID() + infrastructureGenerator.getMobileID() + infrastructureGenerator.getPcID() - 3));
        System.out.println("Total end devices: " + (infrastructureGenerator.getCamID() + infrastructureGenerator.getScreenID() - 2));
        System.out.println("Total components: " + application.getComponents().size());
    }
}
