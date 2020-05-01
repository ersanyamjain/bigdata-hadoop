package nyse;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ClsReduce extends Reducer<NYSESymbolYearWritable, NYSEWritable, Text, Text> 
{
	Text reduceOutKey = new Text();
	Text reduceOutValue = new Text();

	@Override
	protected void reduce(NYSESymbolYearWritable reduceInKey,
			Iterable<NYSEWritable> reduceInValues,Context context)
					throws IOException, InterruptedException 
	{
		// TODO Auto-generated method stub
		System.out.println("In reduce function");
		try
		{
			double maxStockPrice = 0;
			double minStockPrice = Double.MAX_VALUE;
			double sumDividends = 0;

			//NYSEWritable reduceInValue = new NYSEWritable();

			StringBuilder sb = new StringBuilder("");

			int count = 0;

			reduceOutKey.set(reduceInKey.toString());

			for(NYSEWritable reduceInValue : reduceInValues) {

				if (reduceInValue.getFile_identifier().equals(
						"dividends")) {
					sumDividends = sumDividends
							+ reduceInValue.getStock_dividend();
					count++;
				}
				else{
					maxStockPrice = (maxStockPrice > reduceInValue
							.getStock_price_high()) ? maxStockPrice
									: reduceInValue.getStock_price_high();
					minStockPrice = (minStockPrice < reduceInValue
							.getStock_price_low()) ? minStockPrice
									: reduceInValue.getStock_price_low();
				}
			}
			sb.append(",maxStockPrice=");
			sb.append(maxStockPrice);
			sb.append(",minStockPrice=");
			sb.append(minStockPrice);
			sb.append(",avgDividend=");
			sb.append(sumDividends / count);

			reduceOutValue.set(sb.toString());

			context.write(reduceOutKey, reduceOutValue);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}
