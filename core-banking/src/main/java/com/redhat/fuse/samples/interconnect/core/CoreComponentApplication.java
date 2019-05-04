package com.redhat.fuse.samples.interconnect.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.fuse.samples.interconnect.core.datamodels.canonical.AccountUpdateCommand;
import com.redhat.fuse.samples.interconnect.core.datamodels.canonical.LiquidityBalanceTransfer;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootApplication
@RestController
public class CoreComponentApplication {

    List<AccountUpdateCommand> accountUpdateCommands = new CopyOnWriteArrayList<>();

    List<LiquidityBalanceTransfer> liquidityBalanceTransfers = new CopyOnWriteArrayList<>();

    @Value("${connect.url:localhost}")
    String routerUrl;

    @Value("${region.name:localhost}")
    String regionName;

    private SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    @GetMapping("/eventStream")
    @ResponseBody
    SseEmitter eventStream() {
        return sseEmitter;
    }


    @Bean
    public JmsComponent jms(CamelContext camelContext) {
        JmsConnectionFactory jmsConnectionFactory = new JmsConnectionFactory();
        String formattedRemoteUrl = "amqps://" + routerUrl + "?transport.verifyHost=false&transport.trustAll=true";
        jmsConnectionFactory.setRemoteURI(formattedRemoteUrl);
        jmsConnectionFactory.setReceiveLocalOnly(true);
        JmsComponent jmsComponent = new JmsComponent(camelContext);
        jmsComponent.setConnectionFactory(jmsConnectionFactory);
        return jmsComponent;
    }


    @Bean
    public RouteBuilder routeBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("direct:account-updates-publish").to("jms:topic:accountUpdates");

                from("jms:topic:accounts/global")
                        .log("Received account update ${in.body}")
                        .process(new MyProcessor(AccountUpdateCommand.class, accountUpdateCommands, "accountUpdate", sseEmitter));

                from("jms:topic:liquidityBalanceTransfers")
                        .log("Received ${in.body}")
                        .process(new MyProcessor(LiquidityBalanceTransfer.class, liquidityBalanceTransfers, "liquidityBalanceTransfer", sseEmitter));


                restConfiguration()
                        .component("servlet")
                        .bindingMode(RestBindingMode.auto);

                rest("/accountUpdates")
                        .get().route().setBody().constant(accountUpdateCommands).endRest();


                rest("/accountUpdates")
                        .post().route().log("${in.body}").inOnly("direct:account-updates-publish").endRest();


                rest("/liquidityBalanceTransfers")
                        .get().route().setBody().constant(liquidityBalanceTransfers).endRest();

                rest("/liquidityBalanceTransfers/local")
                        .post().route().log("${in.body}").inOnly("jms:topic:liquidityBalanceTransfers/local").endRest();

                rest("/liquidityBalanceTransfers/apac")
                        .post().route().log("${in.body}").inOnly("jms:topic:liquidityBalanceTransfers/apac").endRest();

                rest("/liquidityBalanceTransfers/emea")
                        .post().route().log("${in.body}").inOnly("jms:topic:liquidityBalanceTransfers/emea").endRest();

                rest("/liquidityBalanceTransfers/nam")
                        .post().route().log("${in.body}").inOnly("jms:topic:liquidityBalanceTransfers/nam").endRest();



                rest("/metadata")
                        .get().route().setBody()
                        .constant(new Metadata("amqps://" + routerUrl + ":443",
                                regionName,
                                "accounts/global",
                                "accountUpdates",
                                "liquidityBalanceTransfers",
                                "liquidityBalanceTransfers/local",
                                "liquidityBalanceTransfers/apac",
                                "liquidityBalanceTransfers/emea",
                                "liquidityBalanceTransfers/nam"))
                        .endRest();

            }
        };
    }


    public static void main(String[] args) {
        SpringApplication.run(CoreComponentApplication.class, args);
    }

    private static class MyProcessor<T> implements Processor {

        private Class<T> type;

        private final List<T> commands;

        private String eventName;

        private SseEmitter emitter;

        public MyProcessor(Class<T> type, List<T> commands, String eventName, SseEmitter emitter) {
            this.type = type;
            this.commands = commands;
            this.eventName = eventName;
            this.emitter = emitter;
        }

        @Override
        public void process(Exchange exchange) throws Exception {
            ObjectMapper objectMapper = new ObjectMapper();
            T command = objectMapper.readValue(exchange.getIn().getBody().toString(), type);
            //command.setConnection(exchange.getFromEndpoint().getEndpointUri());
            commands.add(command);
            emitter.send(SseEmitter.event().name(eventName).data(command.toString()));
        }
    }
}
