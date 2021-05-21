package com.example.servingwebcontent;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Queue;
import java.util.TreeMap;

@Controller
public class StasticsController {

	@GetMapping("/")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) throws IOException, InterruptedException {

		Process process = Runtime.getRuntime().exec(new String[]{"sh","-c","sh /root/statics/shop.sh"},null,null);
		InputStreamReader ir = new InputStreamReader(process.getInputStream());
		LineNumberReader input = new LineNumberReader(ir);
		String line;
		TreeMap<Integer,String> map = new TreeMap<Integer,String>();
		process.waitFor();
		while ((line = input.readLine()) != null){
			String[] set = line.split(":");
			if(set.length == 2){
				Integer key = Integer.parseInt(set[0]);
				map.put(key,set[1]);
			}
		}

		model.addAttribute("ht", map);

		ArrayList<String> date = new ArrayList<>();
		ArrayList<Integer> user = new ArrayList<>();
		ArrayList<Integer> game = new ArrayList<>();

		File file = new File("/root/statics/statics");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				String[] set = tempString.split(",");
				if(set.length == 3){
					date.add(set[0]);
					user.add(Integer.parseInt(set[1]));
					game.add(Integer.parseInt(set[2]));
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

//		process = Runtime.getRuntime().exec(new String[]{"sh","-c","sh /root/statics/statics.sh"},null,null);
//		ir = new InputStreamReader(process.getInputStream());
//		input = new LineNumberReader(ir);
//		process.waitFor();
//		while ((line = input.readLine()) != null){
//			String[] set = line.split(",");
//			if(set.length == 3){
//				date.add(set[0]);
//				user.add(Integer.parseInt(set[1]));
//				game.add(Integer.parseInt(set[2]));
//			}
//		}


		model.addAttribute("user", user);
		model.addAttribute("game", game);
		model.addAttribute("date", date);



		if(true) {

			ArrayList<String> mdate = new ArrayList<>();
			ArrayList<String> mmoney = new ArrayList<>();
			process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "sh /root/statics/day_money.sh"}, null, null);
			ir = new InputStreamReader(process.getInputStream());
			input = new LineNumberReader(ir);
			process.waitFor();
			while ((line = input.readLine()) != null) {
				if (line.startsWith("day")) continue;
				if(line.startsWith("total"))
				{
					String[] set = line.split("\t");
					model.addAttribute("mtotal", set[1]);
					continue;
				}
				String[] set = line.split("\t");
				if (set.length == 2) {
					mdate.add(set[0]);
					mmoney.add(set[1]);
				}
			}
			model.addAttribute("mdate", mdate);
			model.addAttribute("mmoney", mmoney);
		}
		return "stastics";
	}
}

