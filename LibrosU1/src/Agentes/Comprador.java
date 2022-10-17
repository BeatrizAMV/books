/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agentes;
import jade.core.Agent;
import Comportamiento.SolicitudEjecucion;
import GUI.CompradorGUI;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Comprador extends Agent {

    private String tituloLibro;
    private AID[] agenteVendedor;
    private int tiempo = 10000;
    private Comprador agent = this;
    private CompradorGUI gui;

    protected void configurar() {
        System.out.println("Agente comprador " + getAID().getName() + " esta listo");

        gui = new CompradorGUI(this);
        gui.showGui();

    }

    protected void takeDown() {
        System.out.println("Agente comprador " + getAID().getName() + " terminando");
    }

    public AID[] getVendedor() {
        return agenteVendedor;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public void intentaComprar(String book) {

        if (book != null && book.length() > 0) {
            tituloLibro = book;
            System.out.println("Libro: " + tituloLibro);

            addBehaviour(new TickerBehaviour(this, tiempo) {
                @Override
                protected void onTick() {
                    System.out.println("Intenta comprar " + tituloLibro);

                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("venta-libro");
                    template.addServices(sd);

                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        System.out.println("Se encontraron los siguientes gentes de ventas:");
                        agenteVendedor = new AID[result.length];
                        for (int i = 0; i < result.length; i++) {
                            agenteVendedor[i] = result[i].getName();
                            System.out.println(agenteVendedor[i].getName());
                        }

                    } catch (FIPAException fe) {
                        fe.printStackTrace();
                    }

                    myAgent.addBehaviour(new SolicitudEjecucion(agent));
                }
            });
        } else {
            System.out.println("No se especificó ningun titulo de libro");
            doDelete();
        }
    }
}
