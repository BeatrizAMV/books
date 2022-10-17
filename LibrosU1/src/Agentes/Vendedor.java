package Agentes;

import java.util.Hashtable;

import Comportamiento.SolicitudOferta;
import Comportamiento.ServidorOrdenesCompra;
import GUI.VendedorGUI;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Vendedor extends Agent {

    private Hashtable catalogo;
    private VendedorGUI gui;

    protected void setup() {
        catalogo = new Hashtable();

        gui = new VendedorGUI(this);
        gui.showGui();

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("Venta-Libro");
        sd.setName("Comercio-Libro");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new SolicitudOferta(this));

        addBehaviour(new ServidorOrdenesCompra(this));
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        gui.dispose();

        System.out.println("Agente vendedor " + getAID().getName() + "terminando");
    }

    public void updateCatalogo(final String titulo, final int precio) {
        addBehaviour(new OneShotBehaviour() {
            public void action() {
                catalogo.put(titulo, precio);
                System.out.println(titulo + " añadido con el precio " + precio);
            }
        });
    }

    public Hashtable getCatalogo() {
        return catalogo;
    }
}
