import controller.GestorAtencion;
import repositorios.HistoriaClinicaRepository;
import view.VistaVeterinario;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA VETERINARIA PATITAS ===");
        System.out.println("Iniciando módulos...");


        HistoriaClinicaRepository hcRepo = new HistoriaClinicaRepository();
        GestorAtencion gestorAtencion = new GestorAtencion(hcRepo, null);


        VistaVeterinario vistaVet = new VistaVeterinario(gestorAtencion);
        vistaVet.mostrarMenu();
    }
}