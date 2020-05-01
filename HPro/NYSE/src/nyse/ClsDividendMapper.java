package nyse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ClsDividendMapper extends Mapper<LongWritable, Text, 
NYSESymbolYearWritable, NYSEWritable> 
{
	NYSESymbolYearWritable mapOutKey = new NYSESymbolYearWritable();
	NYSEWritable mapOutValue = new NYSEWritable();

	@SuppressWarnings("deprecation")
	@Override
	public void map(LongWritable mapInKey,Text mapInValue,Context context) 
	{
		try
		{
			String[] mapInFieldsArray = mapInValue.toString().split(",");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if (mapInFieldsArray.length == 4
					& mapInFieldsArray[0].trim().equals("exchange") == false) {
				/* Set the Map Output Key Variables */
				mapOutKey.setStock_symbol(mapInFieldsArray[1]);
				try {
					mapOutKey.setStock_year(df.parse(mapInFieldsArray[2]).getYear() + 1900);
				} catch (ParseException e) {
					System.out.println("Not a valid date : "+ mapInFieldsArray[2]);
					e.printStackTrace();
				}
				/* Set the Map Output Values Variables */
				mapOutValue.setFile_identifier("dividends");
				mapOutValue.setStock_date(mapInFieldsArray[2]);
				mapOutValue.setStock_dividend(Double.valueOf(mapInFieldsArray[3]));
				mapOutValue.setStock_exchange(mapInFieldsArray[0]);
				mapOutValue.setStock_price_adj_close(0.0);
				mapOutValue.setStock_price_close(0.0);
				mapOutValue.setStock_price_high(0.0);
				mapOutValue.setStock_price_low(0.0);
				mapOutValue.setStock_price_open(0.0);
				mapOutValue.setStock_symbol(mapInFieldsArray[1]);
				mapOutValue.setStock_volume(0);

				context.write(mapOutKey, mapOutValue);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}