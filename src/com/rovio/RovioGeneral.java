package com.rovio;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import sun.misc.BASE64Encoder;

public class RovioGeneral {
	int TIMEOUT_RovioGeneral = 100;
	int TIMEOUT_READ_RovioGeneral = 100;
	String RovioMjpegauthorizationString = null;
	String rovioaddress = null;
	String rovioport = null;
	int navsignal = 0;
	String navsignalDesc = null;
	int navx = 0;
	int navy = 0;
	double navt = 0.0D;
	int beaconx = 0;
	boolean irdetect = false;
	String irdetectDesc = null;
	int risoluzione = 0;
	String risoluzioneDesc = null;
	int wifisignal = 0;
	String wifisignalDesc = null;
	int head_pos = 0;
	String head_posDesc = null;
	int batteria = 0;
	String batteriaDesc = null;
	boolean batteriaAler = false;
	String dockingstatus = null;

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
		this.TIMEOUT_RovioGeneral = timeout;
		this.TIMEOUT_READ_RovioGeneral = timeout_read;
	}

	public void settime() {
		Date d = new Date();
		int date = (int) (d.getTime() / 1000L);
		requesthttp("/SetTime.cgi?Sec1970=" + date);
	}

	public void updateStatus() {
		try {
			String status = null;

			status = "";
			URL url = new URL("http://" + this.rovioaddress + ":"
					+ this.rovioport + "/rev.cgi?Cmd=nav&action=1");
			URLConnection urlConnection = url.openConnection();
			urlConnection.setConnectTimeout(this.TIMEOUT_RovioGeneral);
			urlConnection.setReadTimeout(this.TIMEOUT_READ_RovioGeneral);
			urlConnection.setUseCaches(false);
			urlConnection.setAllowUserInteraction(false);
//			urlConnection.addRequestProperty("authorization",
//					this.RovioMjpegauthorizationString);
			InputStream inputStream = urlConnection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					inputStream));
			status = in.readLine().toString();
			status = in.readLine().toString();
			String[] stato_split = status.split("\\|");
			this.navx = Integer.parseInt(stato_split[1].substring(2));
			this.navy = Integer.parseInt(stato_split[2].substring(2));
			this.navt = Double.parseDouble(stato_split[3].substring(6));

			this.navsignal = Integer.parseInt(stato_split[5].substring(3));
			if (this.navsignal < 2000) {
				this.navsignalDesc = "NAV: no signal";
			}
			if (((this.navsignal > 2000 ? 1 : 0) & (this.navsignal < 4000 ? 1
					: 0)) != 0) {
				this.navsignalDesc = "NAV: low";
			}
			if (((this.navsignal > 4000 ? 1 : 0) & (this.navsignal < 7000 ? 1
					: 0)) != 0) {
				this.navsignalDesc = "NAV: mid";
			}
			if (((this.navsignal > 7000 ? 1 : 0) & (this.navsignal < 10000 ? 1
					: 0)) != 0) {
				this.navsignalDesc = "NAV: high";
			}
			if (this.navsignal > 10000) {
				this.navsignalDesc = "NAV: max";
			}

			status = in.readLine().toString();

			stato_split = status.split("\\|");
			this.beaconx = Integer.parseInt(stato_split[1].substring(7));

			status = in.readLine().toString();
			stato_split = status.split("\\|");
			int flags = Integer.parseInt(stato_split[5].substring(6));
			int irdetectstato = 0;
			if (flags == 2) {
				irdetectstato = 1;
			}
			if (flags == 3) {
				irdetectstato = 1;
			}
			if (flags == 6) {
				irdetectstato = 1;
			}
			if (flags == 7) {
				irdetectstato = 1;
			}
			if (irdetectstato == 1) {
				this.irdetect = true;
				this.irdetectDesc = "OBSTACLE";
			} else {
				this.irdetect = false;
				this.irdetectDesc = "IR DETECTING";
			}

			status = in.readLine().toString();
			stato_split = status.substring(1).split("\\|");
			this.risoluzione = Integer.parseInt(stato_split[1].substring(11));
			if (this.risoluzione == 0) {
				this.risoluzioneDesc = "RES: 176x144";
			}
			if (this.risoluzione == 1) {
				this.risoluzioneDesc = "RES: 320x240";
			}
			if (this.risoluzione == 2) {
				this.risoluzioneDesc = "RES: 352x288";
			}
			if (this.risoluzione == 3) {
				this.risoluzioneDesc = "RES: 640x480";
			}

			status = in.readLine().toString();
			stato_split = status.split("\\|");
			int mic_vol = Integer.parseInt(stato_split[4].substring(11));
			int spk_vol = Integer.parseInt(stato_split[3].substring(15));

			status = in.readLine().toString();
			stato_split = status.substring(1).split("\\|");
			this.wifisignal = Integer.parseInt(stato_split[0].substring(8));
			if (this.wifisignal < 166)
				this.wifisignalDesc = "WIFI: warning, low";
			else {
				this.wifisignalDesc = ("WIFI: " + this.wifisignal);
			}

			status = in.readLine().toString();
			stato_split = status.split("\\|");

			this.head_pos = Integer.parseInt(stato_split[3].substring(14));
			this.head_posDesc = "High - HEAD - Low";

			this.batteria = Integer.parseInt(stato_split[1].substring(8));
			if (this.batteria < 101) {
				this.batteriaDesc = "BATT: DEAD";
			}
			if (((this.batteria > 101 ? 1 : 0) & (this.batteria < 106 ? 1 : 0)) != 0) {
				this.batteriaDesc = "BATT: GO HOME";
			}
			if (this.batteria > 106) {
				this.batteriaDesc = ("BATT: " + this.batteria);
			}
			if (((this.batteria > 101 ? 1 : 0) & (this.batteria < 108 ? 1 : 0)) != 0)
				this.batteriaAler = true;
			else {
				this.batteriaAler = false;
			}

			int batt_status = Integer.parseInt(stato_split[2].substring(9));
			if (batt_status == 60) {
				this.batteriaDesc = "NOT CHARGING";
				this.dockingstatus = "DOCKED";
			}
			if (batt_status == 80) {
				this.batteriaDesc = "CHARGING";
				this.dockingstatus = "DOCKED";
				this.batteria = 0;
			}
			if (batt_status == 72) {
				this.batteriaDesc = "BATT: FULL";
				this.dockingstatus = "DOCKED";
				this.batteria = 127;
			}
			if (batt_status == 0) {
				this.dockingstatus = "ROAMING";
			}

			in.close();
			inputStream.close();
		} catch (IOException e) {
		}
	}

	public Boolean checkconnection() {
		try {
			URL url = new URL("http://" + this.rovioaddress + ":"
					+ this.rovioport);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setConnectTimeout(5000);
			urlConnection.setReadTimeout(5000);
			urlConnection.setUseCaches(false);
			urlConnection.setAllowUserInteraction(false);
//			urlConnection.addRequestProperty("authorization",
//					this.RovioMjpegauthorizationString);
			urlConnection.getInputStream();
			return Boolean.valueOf(true);
		} catch (IOException e) {
			System.out.println("Errore di connessione");
		}
		return Boolean.valueOf(false);
	}

	public void requesthttp(String requestUrl) {
		try {
			URL url = new URL("http://" + this.rovioaddress + ":"
					+ this.rovioport + requestUrl.toString());
			URLConnection urlConnection = url.openConnection();
			urlConnection.setConnectTimeout(this.TIMEOUT_RovioGeneral);
			urlConnection.setReadTimeout(this.TIMEOUT_READ_RovioGeneral);
			urlConnection.setUseCaches(false);
			urlConnection.setAllowUserInteraction(false);
//			urlConnection.addRequestProperty("authorization",
//					this.RovioMjpegauthorizationString);
			urlConnection.getInputStream();
		} catch (IOException e) {
		}
	}
	
	public void printStatus(){
		System.out.println("navx="+this.navx+",navy="+this.navy);
		System.out.println("navsignal="+this.navsignal+" navsignalDesc="+this.navsignalDesc);
		System.out.println("beaconx="+this.beaconx);
		System.out.println("irdetect="+this.irdetect+" irdetectDesc="+this.irdetectDesc);
		System.out.println("risoluzioneDesc="+this.risoluzioneDesc);
		System.out.println("wifisignal="+this.wifisignal+" wifisignalDesc"+this.wifisignalDesc);
		System.out.println("batteria="+this.batteria+" batteriaDesc="+this.batteriaDesc);
		System.out.println("dockingstatus="+this.dockingstatus);
	}
	
	public static void main(String[] args){
		RovioGeneral rovio=new RovioGeneral();
		rovio.setaddress("192.168.10.20", "80");
		System.out.println(rovio.checkconnection());
		rovio.updateStatus();
		rovio.printStatus();
	}
}