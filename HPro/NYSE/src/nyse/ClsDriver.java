package nyse;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class ClsDriver extends Configured implements Tool 
{

	@Override
	public int run(String[] arg0) throws Exception
	{
		  Job job = Job.getInstance(new Configuration());
		  job.setJobName("NYSE Yearly Analysis");

		  job.setJarByClass(getClass());
		  
		  job.setReducerClass(ClsReduce.class);
		  
		  // Output Key-Value Data types
		  job.setMapOutputKeyClass(NYSESymbolYearWritable.class);
		  job.setMapOutputValueClass(NYSEWritable.class);

		  job.setOutputKeyClass(Text.class);
		  job.setOutputValueClass(Text.class);

		  // Inform Input/Output Formats and Directory Locations

		  /*
		   * Since there are two mappers here for two different files, You need to
		   * define which mapper processes which input file. Refer to Point 2 in
		   * Design Considerations.
		   */

		  
		  MultipleInputs.addInputPath(job, new Path(arg0[0]), 
				  TextInputFormat.class,ClsPriceMapper.class);
		  
		  MultipleInputs.addInputPath(job, new Path(arg0[1]), 
				  TextInputFormat.class,ClsDividendMapper.class);
		  
		  
		  //MultipleOutputs.addNamedOutput(job, "year", TextOutputFormat.class, Text.class, Text.class);
		  
		  FileOutputFormat.setOutputPath(job, new Path(arg0[2]));
		  
		  System.out.println("Reducer class" + job.getReducerClass());
		  System.out.println("Mapper class" + job.getMapperClass());
		  
		  return job.waitForCompletion(true)?1:0;
	}

	public static void main(String[] args) 
	{
		try
		{
			ClsDriver driver = new ClsDriver();
			ToolRunner.run(driver.getConf(), driver, args);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

}
