package nyse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ClsPriceMapper extends Mapper<LongWritable, Text, 
NYSESymbolYearWritable, NYSEWritable>
{
	NYSESymbolYearWritable mapOutKey = new NYSESymbolYearWritable();
	NYSEWritable mapOutValue = new NYSEWritable();

	@SuppressWarnings("deprecation")
	@Override
	public void map(LongWritable mapInKey, Text mapInValue,Context context)

	{
		try
		{
			String[] mapInFieldsArray = mapInValue.toString().split(",");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			if (mapInFieldsArray.length == 9
					& mapInFieldsArray[0].trim().equals("exchange") == false) {

				/* Set the Map Output Key Variables */
				mapOutKey.setStock_symbol(mapInFieldsArray[1]);
					
				try {
					/*
					 * Note that the getYear method returns the year subtracting
					 * 1900 from the year value. And hence we need to add 1900
					 * to output year
					 */
					mapOutKey.setStock_year(df.parse(mapInFieldsArray[2]).getYear() + 1900);
					
				} 
				catch (ParseException e) {
					System.out.println("Not a valid date : "
							+ mapInFieldsArray[2]);
					e.printStackTrace();
				}
				/* Set the Map Output Values Variables */
				mapOutValue.setFile_identifier("prices");
				mapOutValue.setStock_date(mapInFieldsArray[2]);
				mapOutValue.setStock_dividend(0.0);
				mapOutValue.setStock_exchange(mapInFieldsArray[0]);
				mapOutValue.setStock_price_adj_close(Double.valueOf(mapInFieldsArray[8]));
				mapOutValue.setStock_price_close(Double.valueOf(mapInFieldsArray[6]));
				mapOutValue.setStock_price_high(Double.valueOf(mapInFieldsArray[4]));
				mapOutValue.setStock_price_low(Double.valueOf(mapInFieldsArray[5]));
				mapOutValue.setStock_price_open(Double.valueOf(mapInFieldsArray[3]));
				mapOutValue.setStock_symbol(mapInFieldsArray[1]);
				mapOutValue.setStock_volume(Integer.valueOf(mapInFieldsArray[7]).longValue());

				context.write(mapOutKey, mapOutValue);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

	}
}
