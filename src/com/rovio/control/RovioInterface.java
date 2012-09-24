package com.rovio.control;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Rovio控制接口
 * 
 * @author GOD
 * 
 */
public class RovioInterface {
	private int TIMEOUT = 5000;
	private int TIMEOUT_READ = 5000;
	private String rovioaddress = null;
	private String rovioport = null;
	public int navsignal = 0;
	public String navsignalDesc = null;
	public int navx = 0;
	public int navy = 0;
	public double navt = 0.0D;
	public int beaconx = 0;
	public boolean irdetect = false;
	public String irdetectDesc = null;
	public int resolution = 0;
	public String resolutionDesc = null;
	public int wifisignal = 0;
	public String wifisignalDesc = null;
	public int head_pos = 0;
	public String head_posDesc = null;
	public int batteria = 0;
	public String batteriaDesc = null;
	public boolean batteriaAler = false;
	public String dockingstatus = null;

	/**
	 * 初始化Rovio控制接口
	 * 
	 * @param address
	 *            Rovio ip地址
	 * @param port
	 *            端口
	 */
	public RovioInterface(String address, String port) {
		setAddress(address, port);
	}

	/**
	 * 设置Rovio的ip和端口
	 * 
	 * @param address
	 *            ip地址
	 * @param port
	 *            端口
	 */
	public void setAddress(String address, String port) {
		this.rovioaddress = address;
		this.rovioport = port;
	}

	/**
	 * 设置http响应时间
	 * 
	 * @param timeout
	 * @param timeout_read
	 */
	public void settimeout(int timeout, int timeout_read) {
		this.TIMEOUT = timeout;
		this.TIMEOUT_READ = timeout_read;
	}

	/**
	 * 测试是否可以连通Rovio
	 * 
	 * @return true，连通；false，无法连通
	 */
	public boolean checkConnection() {
		return null != requestHttp("", 5000, 5000);
	}

	/**
	 * 发送http请求
	 * 
	 * @param requestUrl
	 * @return
	 */
	private InputStream requestHttp(String requestUrl) {
		return requestHttp(requestUrl, this.TIMEOUT, this.TIMEOUT_READ);
	}

	/**
	 * 发送http请求
	 * 
	 * @param requestUrl
	 * @param timeout
	 * @param timeout_read
	 * @return
	 */
	private InputStream requestHttp(String requestUrl, int timeout,
			int timeout_read) {
		try {
			URL url = new URL("http://" + this.rovioaddress + ":"
					+ this.rovioport + requestUrl.toString());
			URLConnection urlConnection = url.openConnection();
			urlConnection.setConnectTimeout(timeout);
			urlConnection.setReadTimeout(timeout_read);
			urlConnection.setUseCaches(false);
			urlConnection.setAllowUserInteraction(false);
			return urlConnection.getInputStream();
		} catch (IOException e) {
		}
		return null;
	}

	/**
	 * 更新Rovio当前状态
	 */
	public void updateStatus() {
		try {
			String status = null;

			status = "";
			InputStream inputStream = requestHttp("/rev.cgi?Cmd=nav&action=1");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					inputStream));

			status = in.readLine().toString();
			status = in.readLine().toString();
			String[] status_split = status.split("\\|");
			this.navx = Integer.parseInt(status_split[1].substring(2));
			this.navy = Integer.parseInt(status_split[2].substring(2));
			this.navt = Double.parseDouble(status_split[3].substring(6));
			this.navsignal = Integer.parseInt(status_split[5].substring(3));
			if (this.navsignal < 2000) {
				this.navsignalDesc = "NAV: no signal";
			} else if (2000 <= this.navsignal && this.navsignal < 4000) {
				this.navsignalDesc = "NAV: low";
			} else if (4000 <= this.navsignal && this.navsignal < 7000) {
				this.navsignalDesc = "NAV: mid";
			} else if (7000 <= this.navsignal && this.navsignal < 10000) {
				this.navsignalDesc = "NAV: high";
			} else if (this.navsignal >= 10000) {
				this.navsignalDesc = "NAV: max";
			}

			status = in.readLine().toString();
			status_split = status.split("\\|");
			this.beaconx = Integer.parseInt(status_split[1].substring(7));

			status = in.readLine().toString();
			status_split = status.split("\\|");
			int flags = Integer.parseInt(status_split[5].substring(6));
			int irdetectstato = 0;
			if (flags == 2 || flags == 3 || flags == 6 || flags == 7) {
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
			status_split = status.substring(1).split("\\|");
			this.resolution = Integer.parseInt(status_split[1].substring(11));
			if (this.resolution == 0) {
				this.resolutionDesc = "RES: 176x144";
			} else if (this.resolution == 1) {
				this.resolutionDesc = "RES: 320x240";
			} else if (this.resolution == 2) {
				this.resolutionDesc = "RES: 352x288";
			} else if (this.resolution == 3) {
				this.resolutionDesc = "RES: 640x480";
			}

			status = in.readLine().toString();
			status_split = status.split("\\|");
			int mic_vol = Integer.parseInt(status_split[4].substring(11));
			int spk_vol = Integer.parseInt(status_split[3].substring(15));

			status = in.readLine().toString();
			status_split = status.substring(1).split("\\|");
			this.wifisignal = Integer.parseInt(status_split[0].substring(8));
			if (this.wifisignal < 166)
				this.wifisignalDesc = "WIFI: warning, low";
			else {
				this.wifisignalDesc = ("WIFI: " + this.wifisignal);
			}

			status = in.readLine().toString();
			status_split = status.split("\\|");
			this.head_pos = Integer.parseInt(status_split[3].substring(14));
			this.head_posDesc = "High - HEAD - Low";
			this.batteria = Integer.parseInt(status_split[1].substring(8));
			if (this.batteria < 101) {
				this.batteriaDesc = "BATT: DEAD";
			} else if (101 <= this.batteria && this.batteria < 106) {
				this.batteriaDesc = "BATT: GO HOME";
			} else if (this.batteria > 106) {
				this.batteriaDesc = ("BATT: " + this.batteria);
			}
			if (101 <= this.batteria && this.batteria < 108)
				this.batteriaAler = true;
			else {
				this.batteriaAler = false;
			}

			int batt_status = Integer.parseInt(status_split[2].substring(9));
			if (batt_status == 60) {
				this.batteriaDesc = "NOT CHARGING";
				this.dockingstatus = "DOCKED";
			} else if (batt_status == 80) {
				this.batteriaDesc = "CHARGING";
				this.dockingstatus = "DOCKED";
				this.batteria = 0;
			} else if (batt_status == 72) {
				this.batteriaDesc = "BATT: FULL";
				this.dockingstatus = "DOCKED";
				this.batteria = 127;
			} else if (batt_status == 0) {
				this.dockingstatus = "ROAMING";
			}

			in.close();
			inputStream.close();
		} catch (IOException e) {
		}
	}
	
	public void testGetStatus(){
		try {
			String status = null;

			status = "";
			InputStream inputStream = requestHttp("/ScanWlan.cgi");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					inputStream));
			while((status=in.readLine())!=null){
				System.out.println(status);
			}
			in.close();
			inputStream.close();
		} catch (IOException e) {
		}
	}

	/**
	 * 打印Rovio当前状态
	 */
	public void printStatus() {
		System.out.println("navx=" + this.navx + ",navy=" + this.navy);
		System.out.println("navsignal=" + this.navsignal + " navsignalDesc="
				+ this.navsignalDesc);
		System.out.println("beaconx=" + this.beaconx);
		System.out.println("irdetect=" + this.irdetect + " irdetectDesc="
				+ this.irdetectDesc);
		System.out.println("risoluzioneDesc=" + this.resolutionDesc);
		System.out.println("wifisignal=" + this.wifisignal + " wifisignalDesc"
				+ this.wifisignalDesc);
		System.out.println("batteria=" + this.batteria + " batteriaDesc="
				+ this.batteriaDesc);
		System.out.println("dockingstatus=" + this.dockingstatus);
	}
	
	
	public void move(int direction, int speed) {
		requestHttp("/rev.cgi?Cmd=nav&action=18&drive="+direction+"&speed=" + speed);
	}
	
	public void moveAndStop(int direction, int speed) {
		requestHttp("/Cmd.cgi?Cmd=nav&action=18&drive="+direction+"&speed=" + speed+"&Cmd=nav&action=18&drive=0&speed=1");
	}
	
	public void moveWithAngle(int direction, int speed,int angle) {
		requestHttp("/rev.cgi?Cmd=nav&action=18&drive="+direction+"&speed=" + speed+"&angle="+angle);
	}
	
	public static void  test1(){
		RovioInterface rovio = new RovioInterface("192.168.10.18", "80");
		System.out.println(rovio.checkConnection());
		rovio.updateStatus();
		rovio.printStatus();
		rovio.move(Direction.FORWARD, 1);
	}
	
	public static void test2(){
		RovioInterface rovio = new RovioInterface("192.168.10.18", "80");
		System.out.println(rovio.checkConnection());
		rovio.testGetStatus();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		test2();
	}

}
