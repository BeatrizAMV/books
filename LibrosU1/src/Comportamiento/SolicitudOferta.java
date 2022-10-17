package Comportamiento;

import Agentes.Vendedor;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class SolicitudOferta extends CyclicBehaviour {

    Vendedor bsAgente;

    public SolicitudOferta(Vendedor a) {
        bsAgente = a;
    }

    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage msg = bsAgente.receive(mt);

        if (msg != null) {
            String titulo = msg.getContent();
            ACLMessage respuesta = msg.createReply();

            Integer price = (Integer) bsAgente.getCatalogo().get(titulo);

            if (price != null) {
                respuesta.setPerformative(ACLMessage.PROPOSE);
                respuesta.setContent(String.valueOf(price.intValue()));
            } else {
                respuesta.setPerformative(ACLMessage.REFUSE);
                respuesta.setContent("No disponible");
            }

            bsAgente.send(respuesta);
        } else {
            block();
        }
    }
}
