package maze;

public class AlgorithmController implements Runnable {
	protected boolean IsPause=false;
	protected boolean IsStop=false;
	AlgorithmController()
	{
		IsPause=false;
		IsStop=false;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
//Controller
	public void Pause() {
		IsPause=true;
	}
	public void Continue()
	{
		IsPause=false;
	}
	public void Stop()
	{
		IsStop=true;
	}
//get
	public boolean getIsPause()
	{
		return IsPause;
	}
}
