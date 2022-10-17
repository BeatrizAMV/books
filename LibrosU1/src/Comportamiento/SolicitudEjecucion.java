package Comportamiento;

import Agentes.Comprador;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class SolicitudEjecucion extends Behaviour {

    private AID mejorVendido;
    private int mejorPrecio;
    private int numRespuesta = 0;
    private MessageTemplate mt;
    private int opc = 0;
    private Comprador bbAgent;
    private String tituloLibro;

    public SolicitudEjecucion(Comprador a) {
        bbAgent = a;
        tituloLibro = a.getTituloLibro();
    }

    public void action() {
        switch (opc) {
            case 0:
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                for (int i = 0; i < bbAgent.getVendedor().length; i++) {
                    cfp.addReceiver(bbAgent.getVendedor()[i]);
                }

                cfp.setContent(tituloLibro);
                cfp.setConversationId("book-trade");
                cfp.setReplyWith("cfp" + System.currentTimeMillis());
                myAgent.send(cfp);

                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
                        MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                opc = 1;
                break;

            case 1:
                ACLMessage reply = bbAgent.receive(mt);
                if (reply != null) {
                    if (reply.getPerformative() == ACLMessage.PROPOSE) {
                        int price = Integer.parseInt(reply.getContent());
                        if (mejorVendido == null || price < mejorPrecio) {
                            mejorPrecio = price;
                            mejorVendido = reply.getSender();
                        }
                    }
                    numRespuesta++;
                    if (numRespuesta >= bbAgent.getVendedor().length) {
                        opc = 2;
                    }
                } else {
                    block();
                }
                break;

            case 2:
                ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                order.addReceiver(mejorVendido);
                order.setContent(tituloLibro);
                order.setConversationId("book-trade");
                order.setReplyWith("order" + System.currentTimeMillis());
                bbAgent.send(order);

                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
                        MessageTemplate.MatchInReplyTo(order.getReplyWith()));

                opc = 3;

                break;

            case 3:
                reply = myAgent.receive(mt);
                if (reply != null) {
                    if (reply.getPerformative() == ACLMessage.INFORM) {
                        System.out.println(tituloLibro + " compra exitosa del agente  " + reply.getSender().getName());
                        System.out.println("Precio = " + mejorPrecio);
                        myAgent.doDelete();
                    } else {
                        System.out.println("Intento fallido. Ellibro solicitado ya ha sido vendido.");
                    }

                    opc = 4;
                } else {
                    block();
                }
                break;
        }
    }

    public boolean done() {
        if (opc == 2 && mejorVendido == null) {
            System.out.println("Attempt failed: " + tituloLibro + " not available for sale");
        }
        return ((opc == 2 && mejorVendido == null) || opc == 4);
    }
}