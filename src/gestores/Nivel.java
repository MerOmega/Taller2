package gestores;

import java.util.LinkedList;

import entes.Entidad;
import entes.Estructuras.Silo;
import entes.Misiles.MBTonto;

public class Nivel {
	private static Nivel nivel;

	static int MisilesPorNivelmax = 17;
	static int MisilesPorNivelmin = 12;

	private static int nroNivel;
	private static int puntaje;
	private static int puntBonus;
	LinkedList<MBTonto> misiles = new LinkedList<MBTonto>();
	static LinkedList<Entidad> estructuras = Gestor.estructuras;

	private Nivel() {
		puntaje = 0;
		Nivel.nroNivel = 1;

	}

	public static Nivel getNivel() {
		if (nivel == null) {
			nivel = new Nivel();
			// Recibo las ciudades iniciadas, luego cada vez que termino el nivel verifico
			// si hay bonus city para reconstruirlas
		}
		return nivel;
	}

	public void initMisiles(double x, double y) {
		int MisilesPorNivel = (int) (Math.random() * MisilesPorNivelmax + MisilesPorNivelmin);
		for (int i = 0; i < MisilesPorNivel; i++) {
			misiles.add(new MBTonto(x, y, false));
			misiles.get(i).generarDestino();
		}
	}

	private static void reiniciar() {
		puntaje = puntBonus = 0;
		

	}

	public void empezarSimulacion() {
		while(Gestor.juegoTerminado!=true) {
		setPuntaje();
		Gestor.restartCity(estructuras, puntBonus,puntaje);
		evaluar();
		siguienteNivel();
		}
	}
	
	public void evaluar() {
		boolean evaluarEstructuras=true;
		for(int i=0;i<estructuras.size();i++) {
			//Hay al menos una ciudad en pie el juego continua
			if(estructuras.get(i).getClass().getSimpleName().equals("Ciudad") && estructuras.get(i).isDestruida()==false) {
				evaluarEstructuras=false;
			}
		}
		if(evaluarEstructuras==true) {
			Gestor.juegoTerminado=true;
		}
	}

	public static void siguienteNivel() {
		nroNivel++;
		reiniciar();
	}

	
	///////////////////////////////////////////////// Referente a puntajes
	public static int getPuntaje() {
		puntaje = puntaje * nroNivel;
		return puntaje;
	}

	// para el caso cuando explota los misiles
	public void setPuntaje(int valor) {
		Nivel.puntaje += valor;
	}

	public void setPuntaje() {
		for (int i = 0; i < Nivel.estructuras.size(); i++) {
			// Si la clase es silo
			if (Nivel.estructuras.get(i).getClass().getSimpleName().equals("Silo")) {
				Silo silo = (Silo) Nivel.estructuras.get(i);
				Nivel.puntaje = silo.getPuntaje();
			} else if (Nivel.estructuras.get(i).getClass().getSimpleName().equals("Ciudad")
					&& Nivel.estructuras.get(i).isDestruida()) {
				Nivel.puntaje += Nivel.estructuras.get(i).getPuntaje();
			}
		}
		puntBonus = puntaje / Gestor.bonusCity;
	}

	//////////////////////////////////////////////// Fin Puntaje

}
