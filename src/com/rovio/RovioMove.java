package com.rovio;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import sun.misc.BASE64Encoder;

public class RovioMove {
	int TIMEOUT_RovioMove = 100;
	int TIMEOUT_READ_RovioMove = 100;
	String RovioMjpegauthorizationString = null;
	Boolean movement2led = Boolean.valueOf(false);
	String rovioaddress = null;
	String rovioport = null;

	public void setauth(String username, String password) {
		String profileAndPassword = username + ":" + password;
		String encoding = new BASE64Encoder().encode(profileAndPassword
				.getBytes());
		this.RovioMjpegauthorizationString = ("Basic " + new String(encoding));
	}

	public void setaddress(String address, String port) {
		this.rovioaddress = address;
		this.rovioport = port;
	}

	public void settimeout(int timeout, int timeout_read) {
		this.TIMEOUT_RovioMove = timeout;
		this.TIMEOUT_READ_RovioMove = timeout_read;
	}

	public void move(String direction, int velocity) {
		if (direction.equals("forward")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=1&speed=" + velocity);
		}
		if (direction.equals("backward")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=2&speed=" + velocity);
		}
		if (direction.equals("straight right")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=4&speed=" + velocity);
		}
		if (direction.equals("straight left")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=3&speed=" + velocity);
		}
		if (direction.equals("diagonal forward right")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=8&speed=" + velocity);
		}
		if (direction.equals("diagonal forward left")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=7&speed=" + velocity);
		}
		if (direction.equals("diagonal backward right")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=10&speed=" + velocity);
		}
		if (direction.equals("diagonal backward left")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=9&speed=" + velocity);
		}
		if (direction.equals("rotate right by speed")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=6&speed=" + velocity);
		}
		if (direction.equals("rotate left by speed")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=5&speed=" + velocity);
		}
		if (direction.equals("rotate right by 20 degree angle increments")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=18&speed=5&angle=1");
		}
		if (direction.equals("rotate left by 20 degree angle increments")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=17&speed=5&angle=1");
		}
		if (direction.equals("ruota90")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=18&speed=6&angle=7");
		}
		if (direction.equals("ruota180")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=18&speed=6&angle=14");
		}
		if (direction.equals("ruota270")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=17&speed=6&angle=7");
		}
		if (direction.equals("stop")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=0");
		}
		if (this.movement2led.booleanValue()) {
			if (direction.equals("avanti")) {
				imposta_led(16);
			}
			if (direction.equals("indietro")) {
				imposta_led(34);
			}
			if (direction.equals("destra")) {
				imposta_led(123);
			}
			if (direction.equals("sinistra")) {
				imposta_led(456);
			}
			if (direction.equals("avantidx")) {
				imposta_led(1);
			}
			if (direction.equals("avantisx")) {
				imposta_led(6);
			}
			if (direction.equals("indietrodx")) {
				imposta_led(3);
			}
			if (direction.equals("indietrosx")) {
				imposta_led(4);
			}
			if (direction.equals("giradx")) {
				imposta_led(12);
			}
			if (direction.equals("girasx")) {
				imposta_led(56);
			}
			if (direction.equals("ruotadx20")) {
				imposta_led(12);
			}
			if (direction.equals("ruotasx20")) {
				imposta_led(56);
			}
			if (direction.equals("ruota90")) {
				imposta_led(12);
			}
			if (direction.equals("ruota180")) {
				imposta_led(12);
			}
			if (direction.equals("ruota270")) {
				imposta_led(56);
			}
			if (direction.equals("stop"))
				imposta_led(8);
		}
	}

	public void gohome() {
		requesthttp("/rev.cgi?Cmd=nav&action=13");
	}

	public void sethome() {
		requesthttp("/rev.cgi?Cmd=nav&action=14");
	}

	public void sendemail() {
		requesthttp("/SendMail.cgi");
	}

	public void cambiahead(String posizione) {
		if (posizione.equals("alto")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=11");
		}
		if (posizione.equals("medio")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=13");
		}
		if (posizione.equals("basso"))
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=12");
	}

	public void trim_testa(String direzione) {
		if (direzione.equals("su")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=11");
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=0");
		}
		if (direzione.equals("giu")) {
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=12");
			requesthttp("/rev.cgi?Cmd=nav&action=18&drive=0");
		}
	}

	public void imposta_led(int ledmode) {
		switch (ledmode) {
		case 1:
			requesthttp("/mcu?parameters=114D4D00010053485254000100011A200000");
		case 12:
			requesthttp("/mcu?parameters=114D4D00010053485254000100011A300000");
		case 16:
			requesthttp("/mcu?parameters=114D4D00010053485254000100011A210000");
		case 2:
			requesthttp("/mcu?parameters=114D4D00010053485254000100011A100000");
		case 3:
			requesthttp("/mcu?parameters=114D4D00010053485254000100011A080000");
		case 34:
			requesthttp("/mcu?parameters=114D4D00010053485254000100011A120000");
		case 123:
			requesthttp("/mcu?parameters=114D4D00010053485254000100011A380000");
		case 456:
			requesthttp("/mcu?parameters=114D4D00010053485254000100011A070000");
		case 4:
			requesthttp("/mcu?parameters=114D4D00010053485254000100011A040000");
		case 5:
			requesthttp("/mcu?parameters=114D4D00010053485254000100011A020000");
		case 56:
			requesthttp("/mcu?parameters=114D4D00010053485254000100011A030000");
		case 6:
			requesthttp("/mcu?parameters=114D4D00010053485254000100011A010000");
		case 7:
			requesthttp("/mcu?parameters=114D4D00010053485254000100011A000000");
		case 8:
			requesthttp("/mcu?parameters=114D4D00010053485254000100011A3F0000");
		case 9:
			requesthttp("/mcu?parameters=114D4D00010053485254000100011A070000");
		case 10:
			requesthttp("/mcu?parameters=114D4D00010053485254000100011A380000");
		}
	}

	public void imposta_luce(int luce) {
		if (luce == 0) {
			requesthttp("/rev.cgi?Cmd=nav&action=19&LIGHT=0");
		}
		if (luce == 1)
			requesthttp("/rev.cgi?Cmd=nav&action=19&LIGHT=1");
	}

	public void requesthttp(String requestUrl) {
		try {
			URL url = new URL("http://" + this.rovioaddress + ":"
					+ this.rovioport + requestUrl.toString());
			URLConnection urlConnection = url.openConnection();
			urlConnection.setConnectTimeout(this.TIMEOUT_RovioMove);
			urlConnection.setReadTimeout(this.TIMEOUT_READ_RovioMove);
			urlConnection.setUseCaches(false);
			urlConnection.setAllowUserInteraction(false);
			urlConnection.addRequestProperty("authorization",
					this.RovioMjpegauthorizationString);
			urlConnection.getInputStream();
		} catch (IOException e) {
		}
	}
}