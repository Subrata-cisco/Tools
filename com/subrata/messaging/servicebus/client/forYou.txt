package com.ranguard.mh;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MissingHourFinder {

	private List<Integer> hours = IntStream.rangeClosed(0, 23)
		    .boxed().collect(Collectors.toList());

	public void findInfo(String fileRepo) throws IOException {
		
		Set<String> set = Files.lines(Paths.get(fileRepo))
				.filter(line -> line.endsWith(".csv.gz"))
				.map(String::trim)
				.sorted()
				.collect(Collectors.toSet());

		Map<Integer, CompositeInfo> map = new HashMap<>();

		set.stream()
				.forEach(
						path -> {
							String[] fileNameArray = path.split("_");
							if (fileNameArray.length != 7) {
								throw new RuntimeException(
										"File is not valid :" + path);
							}

							String hourMonthString = fileNameArray[3];
							if(hourMonthString == null || hourMonthString.length() != 10){
								throw new RuntimeException("hour month string (3rd) word is not according to convention..");
							}

							int year = Integer.valueOf(hourMonthString
									.substring(0, 4));
							int month = Integer.valueOf(hourMonthString
									.substring(4, 6));
							int day = Integer.valueOf(hourMonthString
									.substring(6, 8));
							int hour = Integer.valueOf(hourMonthString
									.substring(8, hourMonthString.length()));

							// System.out.println(hourMonthString +" "+year +
							// " "+ month + " "+ day +" "+ hour);

							CompositeInfo info = map.get(year);
							if (info == null) {
								info = new CompositeInfo();
								map.put(year, info);
							}
							info.addInfo(year, month, day, hour);
						});

		// 2 Iterate and print the missing hours ...
		map.forEach((year, info) -> {
			Year years = info.getYears();
			Map<Integer, Month> months = years.retrieveMonths();
			months.forEach((monNo, month) -> {
				Map<Integer, Day> days = month.retrieveDays();
				Set<Integer> missingHours = new TreeSet<>(hours);
				days.forEach((dayNo, day) -> {
					Set<Integer> hours = day.retrieveHours();
					missingHours.removeAll(hours);
					if (missingHours.size() != 0) {
						missingHours.forEach(hour -> {
							System.out.println("Missing file for Year :" + year
									+ " month :" + monNo + " day :" + dayNo
									+ " hour :" + hour);
						});
					}
				});
				
				
				Set<Integer> missingDays = month.getMissedDays();
				if (missingDays.size() != 0) {
					missingDays.forEach(day -> {
						System.out.println("Missing file for Year :" + year
								+ " month :" + monNo + " day :" + day);
					});
				}
			});
		});
	}

}
