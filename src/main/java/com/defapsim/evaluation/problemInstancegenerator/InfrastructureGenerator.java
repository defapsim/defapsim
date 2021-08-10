package com.defapsim.evaluation.problemInstancegenerator;

import com.defapsim.infrastructure.Infrastructure;
import com.defapsim.infrastructure.devices.ApplicationHostDevice;
import com.defapsim.infrastructure.devices.Device;
import com.defapsim.infrastructure.devices.DeviceConfiguration;
import com.defapsim.infrastructure.devices.DeviceCreator;
import com.defapsim.infrastructure.devices.fognode.FogNodeCreator;
import com.defapsim.infrastructure.devices.clouddevice.CloudServerCreator;
import com.defapsim.infrastructure.devices.enddevice.EndDeviceCreator;
import com.defapsim.infrastructure.links.Link;
import com.defapsim.infrastructure.links.LinkConfiguration;
import com.defapsim.misc.distributions.IntervalDistribution;
import com.defapsim.misc.xlscreator.XLSExporter;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The InfrastructureGenerator scales an infrastructure according to the approach of:
 *
 * Ye Xia, Xavier Etchevers, Loïc Letondeur, Thierry Coupaye, and Frédéric Desprez. Combining
 * hardware nodes and software components ordering-based heuristics for optimizing the placement
 * of distributed IoT applications in the fog. In Proceedings of the 33rd Annual ACM Symposium on
 * Applied Computing, SAC 2018, Pau, France, April 09-13, 2018, pages 751–760. ACM, 2018.
 */
public class InfrastructureGenerator {
    
    private Float minimumCloudPoPLatency;
    private Float maximumCloudPoPLatency;

    private Float minimumPoPPoPLatency;
    private Float maximumPoPPoPLatency;

    private Float minimumBoxPoPLatency;
    private Float maximumBoxPoPLatency;

    private Float minimumBoxEndDevicesLatency;
    private Float maximumBoxEndDevicesLatency;
    
    private Float minimumProcessingSpeed;
    private Float maximumProcessingSpeed;

    private Float minimumMemory;
    private Float maximumMemory;

    private Float minimumComputingPower;
    private Float maximumComputingPower;

    private Integer seed = 187;
    private Workbook evaluation_workbook;
    private Integer relativeDistance = 0;

    public List<Device> getOldDevices() {
        return oldDevices;
    }

    private List<Device> oldDevices;

    public InfrastructureGenerator withMinimumCloudPoPLatency(Float minimumCloudPoPLatency) {
        this.minimumCloudPoPLatency = minimumCloudPoPLatency;
        return this;
    }

    public InfrastructureGenerator withMaximumCloudPoPLatency(Float maximumCloudPoPLatency) {
        this.maximumCloudPoPLatency = maximumCloudPoPLatency;
        return this;
    }

    public InfrastructureGenerator withMinimumPoPPoPLatency(Float minimumPoPPoPLatency) {
        this.minimumPoPPoPLatency = minimumPoPPoPLatency;
        return this;
    }

    public InfrastructureGenerator withMaximumPoPPoPLatency(Float maximumPoPPoPLatency) {
        this.maximumPoPPoPLatency = maximumPoPPoPLatency;
        return this;
    }

    public InfrastructureGenerator withMinimumBoxPoPLatency(Float minimumBoxPoPLatency) {
        this.minimumBoxPoPLatency = minimumBoxPoPLatency;
        return this;
    }

    public InfrastructureGenerator withMaximumBoxPoPLatency(Float maximumBoxPoPLatency) {
        this.maximumBoxPoPLatency = maximumBoxPoPLatency;
        return this;
    }

    public InfrastructureGenerator withMinimumBoxEndDevicesLatency(Float minimumBoxEndDevicesLatency) {
        this.minimumBoxEndDevicesLatency = minimumBoxEndDevicesLatency;
        return this;
    }

    public InfrastructureGenerator withMaximumBoxEndDevicesLatency(Float maximumBoxEndDevicesLatency) {
        this.maximumBoxEndDevicesLatency = maximumBoxEndDevicesLatency;
        return this;
    }

    public InfrastructureGenerator withMinimumProcessingSpeed(Float minimumProcessingSpeed) {
        this.minimumProcessingSpeed = minimumProcessingSpeed;
        return this;
    }

    public InfrastructureGenerator withMaximumProcessingSpeed(Float maximumProcessingSpeed) {
        this.maximumProcessingSpeed = maximumProcessingSpeed;
        return this;
    }

    public InfrastructureGenerator withMinimumMemory(Float minimumMemory) {
        this.minimumMemory = minimumMemory;
        return this;
    }

    public InfrastructureGenerator withMaximumMemory(Float maximumMemory) {
        this.maximumMemory = maximumMemory;
        return this;
    }

    public InfrastructureGenerator withMinimumComputingPower(Float minimumComputingPower) {
        this.minimumComputingPower = minimumComputingPower;
        return this;
    }

    public InfrastructureGenerator withMaximumComputingPower(Float maximumComputingPower) {
        this.maximumComputingPower = maximumComputingPower;
        return this;
    }

    public InfrastructureGenerator withSeed(Integer seed) {
        this.seed = seed;
        return this;
    }

    public InfrastructureGenerator withEvaluationWorkbook(Workbook evaluation_workbook) {
        this.evaluation_workbook = evaluation_workbook;
        return this;
    }

    public InfrastructureGenerator withRelativeDistance(Integer relativeDistance) {
        this.relativeDistance = relativeDistance;
        return this;
    }

    private Infrastructure infrastructure;

    private ApplicationHostDevice cloud;
    private ApplicationHostDevice PoP1;
    private ApplicationHostDevice PoP2;
    private ApplicationHostDevice PoP3;

    private ApplicationHostDevice Box1;
    private ApplicationHostDevice Box2;
    private ApplicationHostDevice Box3;

    private ApplicationHostDevice Mobile1;
    private ApplicationHostDevice Mobile2;
    private ApplicationHostDevice Mobile3;

    private ApplicationHostDevice PC1;

    private Device Camera1;
    private Device Camera2;
    private Device Camera3;
    private Device Screen1;
    private Device Screen2;
    private Device Screen3;

    private Integer linkID = 1;
    private Integer mobileID = 1;
    private Integer screenID = 1;
    private Integer camID = 1;
    private Integer pcID = 1;
    private Integer boxID = 1;
    private Integer popID = 1;

    final DeviceCreator endDeviceCreator = new EndDeviceCreator();
    final FogNodeCreator fogNodeCreator = new FogNodeCreator();
    final CloudServerCreator cloudServerCreator = new CloudServerCreator();

    private DeviceConfiguration deviceConfiguration;

    private IntervalDistribution intervalDistributionMemoryCapacity;
    private IntervalDistribution intervalDistributionComputingPowerCapacity;
    private IntervalDistribution intervalDistributionProcessingSpeed;
    private IntervalDistribution intervalDistributionCloudPoPLink;
    private IntervalDistribution intervalDistributionPopPoPLink;
    private IntervalDistribution intervalDistributionBoxPoPLink;
    private IntervalDistribution intervalDistributionBoxEndDeviceLink;

    /**
     * Create the initial infrastructure
     * @param resolveRoutes     specifies whether the routes are to be resolved (is relevant for the scaling of the infrastructure by the other phases)
     * @return                  the initial infrastructure according to Xia et al.
     */
    public Infrastructure build(boolean resolveRoutes) {

        this.linkID = 1;
        this.mobileID = 1;
        this.screenID = 1;
        this.camID = 1;
        this.pcID = 1;
        this.boxID = 1;
        this.popID = 1;

        this.infrastructure = new Infrastructure();
        this.deviceConfiguration = new DeviceConfiguration().withInfrastructure(infrastructure);

        this.intervalDistributionMemoryCapacity = new IntervalDistribution(this.minimumMemory.doubleValue() / 2, this.maximumMemory.doubleValue() / 2  + 1, this.seed);
        this.intervalDistributionComputingPowerCapacity = new IntervalDistribution(this.minimumComputingPower.doubleValue() / 2 + 1, this.maximumComputingPower.doubleValue() / 2 + 1, this.seed);
        this.intervalDistributionProcessingSpeed = new IntervalDistribution(this.minimumProcessingSpeed.doubleValue(), this.maximumProcessingSpeed.doubleValue(), this.seed);
        this.intervalDistributionCloudPoPLink = new IntervalDistribution(this.minimumCloudPoPLatency.doubleValue(), this.maximumCloudPoPLatency.doubleValue(), this.seed);
        this.intervalDistributionPopPoPLink = new IntervalDistribution(this.minimumPoPPoPLatency.doubleValue(), this.maximumPoPPoPLatency.doubleValue(), this.seed);
        this.intervalDistributionBoxPoPLink = new IntervalDistribution(this.minimumBoxPoPLatency.doubleValue(), this.maximumBoxPoPLatency.doubleValue(), this.seed);
        this.intervalDistributionBoxEndDeviceLink = new IntervalDistribution(this.minimumBoxEndDevicesLatency.doubleValue(), this.maximumBoxEndDevicesLatency.doubleValue(), this.seed);

        this.cloud = (ApplicationHostDevice) cloudServerCreator.register(deviceConfiguration.withIdentifier("Cloud-Server")
                .withMemory(Float.MAX_VALUE)
                .withComputingPower(Float.MAX_VALUE)
                .withProcessingSpeed(3.2F));

        this.PoP1 = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("PoP " + popID++)
                .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

        this.PoP2 = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("PoP "+ popID++)
                .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

        this.PoP3 = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("PoP "+ popID++)
                .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

         /*
                HOME 1
         */

        this.Box1 = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("Box " + boxID++)
                .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

        this.Camera1 = endDeviceCreator.register(deviceConfiguration.withIdentifier("Camera " + camID++)
                .withMemory(0.F).withComputingPower(0.F).withProcessingSpeed(0.F));

        this.Screen1 = endDeviceCreator.register(deviceConfiguration.withIdentifier("Screen " + screenID++)
                .withMemory(0.F).withComputingPower(0.F).withProcessingSpeed(0.F));

         /*
                HOME 2
         */

        this.Box2 = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("Box " + boxID++)
                .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

        this.Mobile1 = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("Mobile " + mobileID++)
                .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

        this.Camera2 = endDeviceCreator.register(deviceConfiguration.withIdentifier("Camera " + camID++)
                .withMemory(0.F).withComputingPower(0.F).withProcessingSpeed(0.F));
        this.Screen2 = endDeviceCreator.register(deviceConfiguration.withIdentifier("Screen "+ screenID++)
                .withMemory(0.F).withComputingPower(0.F).withProcessingSpeed(0.F));

         /*
                HOME 3
         */

        this.Box3 = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("Box " + boxID++)
                .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

        this.PC1 = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("PC "+ pcID++)
                .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

        this.Mobile2 = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("Mobile "+ mobileID++)
                .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

        this.Mobile3 = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("Mobile "+ mobileID++)
                .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

        this.Camera3 = endDeviceCreator.register(deviceConfiguration.withIdentifier("Camera " + camID++)
                .withMemory(0.F).withComputingPower(0.F).withProcessingSpeed(0.F));
        this.Screen3 = endDeviceCreator.register(deviceConfiguration.withIdentifier("Screen "+ screenID++)
                .withMemory(0.F).withComputingPower(0.F).withProcessingSpeed(0.F));


        Link.addUndirected(new LinkConfiguration().from(this.cloud).to(this.PoP1).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionCloudPoPLink.getValue().floatValue()));
        Link.addUndirected(new LinkConfiguration().from(this.PoP1).to(this.PoP2).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionPopPoPLink.getValue().floatValue()));
        Link.addUndirected(new LinkConfiguration().from(this.PoP1).to(this.PoP3).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionPopPoPLink.getValue().floatValue()));

        Link.addUndirected(new LinkConfiguration().from(this.PoP2).to(this.Box1).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxPoPLink.getValue().floatValue()));
        Link.addUndirected(new LinkConfiguration().from(this.PoP2).to(this.Box2).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxPoPLink.getValue().floatValue()));
        Link.addUndirected(new LinkConfiguration().from(this.PoP2).to(this.Box3).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxPoPLink.getValue().floatValue()));


        Link.addUndirected(new LinkConfiguration().from(this.Box1).to(this.Camera1).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));
        Link.addUndirected(new LinkConfiguration().from(this.Box1).to(this.Screen1).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));

        Link.addUndirected(new LinkConfiguration().from(this.Box2).to(this.Mobile1).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));
        Link.addUndirected(new LinkConfiguration().from(this.Box2).to(this.Camera2).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));
        Link.addUndirected(new LinkConfiguration().from(this.Box2).to(this.Screen2).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));


        Link.addUndirected(new LinkConfiguration().from(this.Box3).to(this.PC1).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));
        Link.addUndirected(new LinkConfiguration().from(this.Box3).to(this.Mobile2).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));
        Link.addUndirected(new LinkConfiguration().from(this.Box3).to(this.Mobile3).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));
        Link.addUndirected(new LinkConfiguration().from(this.Box3).to(this.Camera3).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));
        Link.addUndirected(new LinkConfiguration().from(this.Box3).to(this.Screen3).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));

        if(resolveRoutes) {
            infrastructure.resolveRoutes();
            XLSExporter.getInstance().buildInfrastructureXLSBlockHeadline(this.seed, this.evaluation_workbook, this.relativeDistance);
            XLSExporter.getInstance().buildInfrastructureXLSBlockDevices("build", this.infrastructure.getDevices(), this.evaluation_workbook, this.relativeDistance, 2);
        }
        return this.infrastructure;
    }

    /**
     * Create the infrastructure for phase 1 according to Xia et al.
     * @param round                 specifies the round in which the infrastructure for phase 1 is to be created
     * @param resolveRoutes         specifies whether the routes are to be resolved (is relevant for the scaling of the infrastructure by the other phases)
     * @return                      the infrastructure for round "round" in phase 1
     */
    public Infrastructure createPhase1(Integer round, boolean resolveRoutes) {
        this.build(false);

        List<Device> tmp = new LinkedList<>();

        for(int i = 0; i < round; i++) {
            ApplicationHostDevice Mobile = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("Mobile " + mobileID++)
                    .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                    .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                    .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

            if(i == round - 1)
                tmp.add(Mobile);

            Link.addUndirected(new LinkConfiguration().from(this.Box1).to(Mobile).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));

            ApplicationHostDevice Mobile2 = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("Mobile "+ mobileID++)
                    .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                    .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                    .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

            if(i == round - 1)
                tmp.add(Mobile2);

            Link.addUndirected(new LinkConfiguration().from(this.Box2).to(Mobile2).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));

            ApplicationHostDevice Mobile3 = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("Mobile "+ mobileID++)
                    .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                    .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                    .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

            if(i == round - 1)
                tmp.add(Mobile3);

            Link.addUndirected(new LinkConfiguration().from(this.Box3).to(Mobile3).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));
        }
        if(resolveRoutes){
            infrastructure.resolveRoutes();
            XLSExporter.getInstance().buildInfrastructureXLSBlockDevices(("1, round " + round), tmp, this.evaluation_workbook, this.relativeDistance, infrastructure.getDevices().size() - tmp.size() + 2 + round);
        }

        this.oldDevices = this.infrastructure.getDevices().stream().filter(device -> !tmp.contains(device)).collect(Collectors.toList());

        return this.infrastructure;
    }


    /**
     * Create the infrastructure for phase 2 according to Xia et al.
     * @param resolveRoutes     specifies whether the routes are to be resolved (is relevant for the scaling of the infrastructure by the other phases)
     * @return                  the infrastructure for for phase 2
     */
    public Infrastructure createPhase2(boolean resolveRoutes) {
        this.createPhase1(10, false);

        List<Device> tmp = new LinkedList<>();

        for(int i = 0; i < 197; i++) {
            ApplicationHostDevice box = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("Box " + boxID++)
                    .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                    .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                    .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

            Device Screen = endDeviceCreator.register(deviceConfiguration.withIdentifier("Screen " + screenID++)
                    .withMemory(0.F).withComputingPower(0.F).withProcessingSpeed(0.F));

            tmp.add(box);
            tmp.add(Screen);

            Link.addUndirected(new LinkConfiguration().from(this.PoP2).to(box).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxPoPLink.getValue().floatValue()));
            Link.addUndirected(new LinkConfiguration().from(box).to(Screen).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));
        }

        if(resolveRoutes) {
            infrastructure.resolveRoutes();
            if(this.evaluation_workbook != null)
                XLSExporter.getInstance().buildInfrastructureXLSBlockDevices(("2"), tmp, this.evaluation_workbook, this.relativeDistance, infrastructure.getDevices().size() - tmp.size() + 2 + 1 + 10);
        }

        this.oldDevices = this.infrastructure.getDevices().stream().filter(device -> !tmp.contains(device)).collect(Collectors.toList());

        return this.infrastructure;
    }

    /**
     * Create the infrastructure for phase 3 according to Xia et al.
     * The number of devices added at this phase is not identical to the number of devices added in the approach of Xia et al.
     * @param round         specifies the round in which the infrastructure for phase 3 is to be created
     * @return              the infrastructure for round "round" in phase 3
     */
    public Infrastructure createPhase3(Integer round) {
        this.createPhase2(false);

        List<Device> tmp = new LinkedList<>();

        List<Device> deviceList = new LinkedList<>();
        deviceList.add(this.PoP1);

        for(int i = 0; i < round; i++) {   //
            ApplicationHostDevice upperPoP = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("PoP " + popID++)
                    .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                    .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                    .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

            deviceList.add(upperPoP);

            if(i == round - 1)
                tmp.add(upperPoP);

            Link.addUndirected(new LinkConfiguration().from(upperPoP).to(this.cloud).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionCloudPoPLink.getValue().floatValue()));


            // Create "j" lowerPoPs for one upperPoP
            for(int j = 0; j < 3; j++) { // 3
                ApplicationHostDevice lowerPoP = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("PoP " + popID++)
                        .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                        .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                        .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

                Link.addUndirected(new LinkConfiguration().from(upperPoP).to(lowerPoP).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionPopPoPLink.getValue().floatValue()));

                if(i == round - 1)
                    tmp.add(lowerPoP);

                // Houses per lowerPoP
                for(int houses = 0; houses < 6; houses++) {

                    ApplicationHostDevice box = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("Box " + boxID++)
                            .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                            .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                            .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

                    ApplicationHostDevice pc = (ApplicationHostDevice) fogNodeCreator.register(deviceConfiguration.withIdentifier("PC " + this.pcID++)
                            .withMemory(Float.valueOf(intervalDistributionMemoryCapacity.getValue().intValue() * 2))
                            .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                            .withComputingPower(Float.valueOf(intervalDistributionComputingPowerCapacity.getValue().intValue() * 2))
                            .withProcessingSpeed(intervalDistributionProcessingSpeed.getValue().floatValue()));

                    Device screen = endDeviceCreator.register(deviceConfiguration.withIdentifier("Screen " + screenID++)
                            .withMemory(0.F).withComputingPower(0.F).withProcessingSpeed(0.F));
                    Device camera = endDeviceCreator.register(deviceConfiguration.withIdentifier("Camera " + this.camID++)
                            .withMemory(0.F).withComputingPower(0.F).withProcessingSpeed(0.F));

                    if(i == round - 1) {
                        tmp.add(box);
                        tmp.add(pc);
                        tmp.add(screen);
                        tmp.add(camera);
                    }

                    Link.addUndirected(new LinkConfiguration().from(lowerPoP).to(box).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxPoPLink.getValue().floatValue()));
                    Link.addUndirected(new LinkConfiguration().from(box).to(pc).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));
                    Link.addUndirected(new LinkConfiguration().from(box).to(screen).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));
                    Link.addUndirected(new LinkConfiguration().from(box).to(camera).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionBoxEndDeviceLink.getValue().floatValue()));
                }
            }
        }

        for(int i = 0; i < deviceList.size(); i++) {
            for(int j = i + 1; j < deviceList.size(); j++) {
                Link.addUndirected(new LinkConfiguration().from(deviceList.get(i)).to(deviceList.get(j)).withNameToFrom("LINK-" + linkID).withNameFromTo("LINK-" + linkID++).withLatencyFromTo(intervalDistributionPopPoPLink.getValue().floatValue()));
            }
        }

        XLSExporter.getInstance().buildInfrastructureXLSBlockDevices(("3, round " + round), tmp, this.evaluation_workbook, this.relativeDistance, infrastructure.getDevices().size() - tmp.size() + 2 + 1 + 11 + round);
        this.oldDevices = this.infrastructure.getDevices().stream().filter(device -> !tmp.contains(device)).collect(Collectors.toList());

        infrastructure.resolveRoutes();
        return this.infrastructure;
    }

    /**
     * Getters
     */

    public Integer getLinkID() {
        return this.linkID;
    }

    public Integer getMobileID() {
        return this.mobileID;
    }

    public Integer getScreenID() {
        return this.screenID;
    }

    public Integer getCamID() {
        return this.camID;
    }

    public Integer getPcID() {
        return this.pcID;
    }

    public Integer getBoxID() {
        return this.boxID;
    }

    public Integer getPopID() {
        return this.popID;
    }
}
