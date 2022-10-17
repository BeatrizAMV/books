package Comportamiento;

import Agentes.Vendedor;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javax.swing.JOptionPane;

public class ServidorOrdenesCompra extends CyclicBehaviour {

    Vendedor bsAgente;

    public ServidorOrdenesCompra(Vendedor a) {
        bsAgente = a;
    }

    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        ACLMessage msg = bsAgente.receive(mt);

        if (msg != null) {
            String title = msg.getContent();
            ACLMessage reply = msg.createReply();

            Integer precio = (Integer) bsAgente.getCatalogo().remove(title);
            if (precio != null) {
                reply.setPerformative(ACLMessage.INFORM);
                System.out.println(title + " vendido al agente " + msg.getSender().getName());
                JOptionPane.showMessageDialog(null, title + " vendido al agente " + msg.getSender().getName(), "Ha sido vendido", JOptionPane.INFORMATION_MESSAGE);
            } else {
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("No disponible");
            }
            bsAgente.send(reply);
        } else {
            block();
        }
    }
}
