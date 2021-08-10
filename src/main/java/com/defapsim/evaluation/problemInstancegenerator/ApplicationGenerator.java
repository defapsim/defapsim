package com.defapsim.evaluation.problemInstancegenerator;

import com.defapsim.application.*;
import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.infrastructure.devices.Device;
import com.defapsim.infrastructure.devices.enddevice.EndDevice;
import com.defapsim.infrastructure.links.Link;
import com.defapsim.misc.xlscreator.XLSExporter;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The ApplicationGenerator scales an application according to the approach of:
 *
 * Ye Xia, Xavier Etchevers, Loïc Letondeur, Thierry Coupaye, and Frédéric Desprez. Combining
 * hardware nodes and software components ordering-based heuristics for optimizing the placement
 * of distributed IoT applications in the fog. In Proceedings of the 33rd Annual ACM Symposium on
 * Applied Computing, SAC 2018, Pau, France, April 09-13, 2018, pages 751–760. ACM, 2018.
 */

public class ApplicationGenerator {

    private Application application;

    private ComponentConfiguration componentConfiguration;

    private Component fire_manager;
    private Component insights_backend;
    private Component machine_learning_engine;

    private Integer fireManagerID = 1;
    private Integer insightsBackendID = 1;
    private Integer machineLearningEngineID = 1;
    private Integer connectorID = 1;

    private List<Device> oldDevices;
    private Float worstCaseExcecutionTimeMultiplier = 1.F;
    private Workbook evaluation_workbook;
    private Integer relativeX = 0;
    private Integer seed = 0;

    public ApplicationGenerator withSeed(Integer seed) {
        this.seed = seed;
        return this;
    }

    public ApplicationGenerator withOldDevices(List<Device> oldDevices) {
        this.oldDevices = oldDevices;
        return this;
    }

    public ApplicationGenerator withRelativeX(Integer relativeX) {
        this.relativeX = relativeX;
        return this;
    }

    public ApplicationGenerator withWorstCaseExcecutionTimeMultiplier(Float worstCaseExcecutionTimeMultiplier) {
        this.worstCaseExcecutionTimeMultiplier = worstCaseExcecutionTimeMultiplier;
        return this;
    }

    public ApplicationGenerator withEvaluationWorkbook(Workbook evaluation_workbook) {
        this.evaluation_workbook = evaluation_workbook;
        return this;
    }

    public Application build() {

        this.fireManagerID = 1;
        this.insightsBackendID = 1;
        this.machineLearningEngineID = 1;
        this.connectorID = 1;

        this.application = new Application("Fire alarm IoT application");
        this.componentConfiguration = new ComponentConfiguration();

        this.fire_manager = Component.createFromConfiguration(componentConfiguration.withIdentifier("FIRE_MANAGER " + this.fireManagerID++).demandMemory(1.F).demandComputingPower(1.F).worstCaseExcecutionTime(25.F * this.worstCaseExcecutionTimeMultiplier)); // ram 11
        this.insights_backend = Component.createFromConfiguration(componentConfiguration.withIdentifier("INSIGHTS_BACKEND " + this.insightsBackendID++).demandMemory(2.F).demandComputingPower(1.F).worstCaseExcecutionTime(50.F * this.worstCaseExcecutionTimeMultiplier)); // 2GB
        this.machine_learning_engine = Component.createFromConfiguration(componentConfiguration.withIdentifier("MACHINE_LEARNING_ENGINE " + this.machineLearningEngineID++).demandMemory(8.F).demandComputingPower(2.F).worstCaseExcecutionTime(90.F * this.worstCaseExcecutionTimeMultiplier)); // 8GB
        this.application.withComponent(this.fire_manager).withComponent(this.insights_backend).withComponent(this.machine_learning_engine);

        return this.application;
    }

    /**
     * The application is generated automatically based on the configuration of the infrastructure
     * @param infrastructure        the infrastructure according to which the application will be created
     * @param probleminstance       the problem instance for which the application should be generated
     * @return
     */
    public Application createApplication(Infrastructure infrastructure, Integer probleminstance) {
        this.build();

        List<Device> boxes = infrastructure.getDevices().stream().filter(device -> device.getIdentifier().contains("Box")).collect(Collectors.toList());

        var fireConnected = 0;
        var machineConnected = 0;

        Component machine_learning_engine = this.machine_learning_engine;
        Component fire_manager = this.fire_manager;

        List<Component> newComponents = new LinkedList<>();

        if(probleminstance == 1) {
            newComponents.addAll(this.application.getComponents());
            Connector.addUndirected(new ConnectorConfiguration<Component, Component>().from(machine_learning_engine).to(this.insights_backend)
                    .withNameFromTo("Connector-" + this.connectorID++));
            Connector.addUndirected(new ConnectorConfiguration<Component, Component>().from(fire_manager).to(this.insights_backend)
                    .withNameFromTo("Connector-" + this.connectorID++));
            Connector.addDirected(new ConnectorConfiguration<Component, Component>().from(fire_manager).to(machine_learning_engine)
                    .withNameFromTo("Connector-" + this.connectorID++));
        }

        for(Device box: boxes){
            if(machineConnected > 8){
                machineConnected = 0;
                machine_learning_engine = Component.createFromConfiguration(this.componentConfiguration.withIdentifier("MACHINE_LEARNING_ENGINE " + this.machineLearningEngineID++).demandMemory(8.F).demandComputingPower(2.F).worstCaseExcecutionTime(90.F * this.worstCaseExcecutionTimeMultiplier)); // 8GB
                if(this.oldDevices != null && !this.oldDevices.contains(box))
                    newComponents.add(machine_learning_engine);
                this.application.withComponent(machine_learning_engine);
                Connector.addUndirected(new ConnectorConfiguration<Component, Component>().from(machine_learning_engine).to(this.insights_backend)
                        .withNameFromTo("Connector-" + this.connectorID++));
            }

            if(fireConnected > 2){
                fireConnected = 0;
                fire_manager = Component.createFromConfiguration(this.componentConfiguration.withIdentifier("FIRE_MANAGER " + this.fireManagerID++).demandMemory(1.F).demandComputingPower(1.F).worstCaseExcecutionTime(25.F * this.worstCaseExcecutionTimeMultiplier)); // 1GB

                if(this.oldDevices != null && !this.oldDevices.contains(box))
                    newComponents.add(fire_manager);

                this.application.withComponent(fire_manager);
                Connector.addUndirected(new ConnectorConfiguration<Component, Component>().from(fire_manager).to(this.insights_backend)
                        .withNameFromTo("Connector-" + this.connectorID++));
                Connector.addDirected(new ConnectorConfiguration<Component, Component>().from(fire_manager).to(machine_learning_engine)
                        .withNameFromTo("Connector-" + this.connectorID++));
            }

            List<Device> endDevices = box.getLinks().stream().map(Link::getTarget).filter(device -> device instanceof EndDevice).collect(Collectors.toList());
            for (Device device : endDevices) {
                Connector.addUndirected(new ConnectorConfiguration<EndDevice, Component>().from((EndDevice) device).to(fire_manager)
                        .withNameFromTo("Connector-" + this.connectorID++));
            }
            machineConnected++;
            fireConnected++;
        }

        if(probleminstance == 1)
            XLSExporter.getInstance().buildApplicationXLSBlockHeadline(this.seed, this.evaluation_workbook, this.relativeX);

        if(this.evaluation_workbook != null)
        XLSExporter.getInstance().buildApplicationXLSBlockComponents("" + probleminstance, newComponents, this.evaluation_workbook,
                this.relativeX, this.application.getComponents().size() - newComponents.size() + 2 + 1 * probleminstance-1);

        return this.application;
    }
}
