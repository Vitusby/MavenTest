import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.sound.midi.*;
import javax.swing.*;

public class BeatBox2
{
	ArrayList<JCheckBox> arraylistCheckBox;
	JFrame mainFrame;
	JPanel mainPanel;
	JPanel centerPanelInmainPanel;
	Box rightBoxInmainPanel;
	Box leftBoxInmainPanel;
	Sequencer sequencer;
	Sequence sequence;
	Track track;
	String sNamesLabels[] = {"Bass Drum","Closed Hi-Hat","Open Hi-Hat","Acoustic Snare",
			"Crash Cymbal","Hand Clap","High Tom","Hi Bongo","Maracas","Whistle","Low Conga",
			"Cowbell","Vibraslap","Low-mid Tom","High Agogo","Open Hi Conga"};
	int nInstrument[] =  {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};
	
	public void buildGUI()
	{
		mainFrame = new JFrame("BeatBox");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		BorderLayout layoutFormainPanel = new BorderLayout();
		mainPanel = new JPanel();
		mainPanel.setLayout(layoutFormainPanel);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		rightBoxInmainPanel = new Box(BoxLayout.Y_AXIS);
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new StartListener());
		rightBoxInmainPanel.add(startButton);
		rightBoxInmainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(new StopListener());
		rightBoxInmainPanel.add(stopButton);
		rightBoxInmainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		JButton tempo_upButton = new JButton("Tempo Up");
		tempo_upButton.addActionListener(new TempoUpListener());
		rightBoxInmainPanel.add(tempo_upButton);
		rightBoxInmainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		JButton tempo_downButton = new JButton("Tempo_Down");
		tempo_downButton.addActionListener(new TempoDownListener());
		rightBoxInmainPanel.add(tempo_downButton);
		mainPanel.add(rightBoxInmainPanel, BorderLayout.EAST);
		
		leftBoxInmainPanel = new Box(BoxLayout.Y_AXIS);
		for(int i=0; i<sNamesLabels.length; i++)
		{
			JLabel label = new JLabel(sNamesLabels[i]);
			leftBoxInmainPanel.add(label);
		}
		mainPanel.add(leftBoxInmainPanel, BorderLayout.WEST);
		

		arraylistCheckBox = new ArrayList<JCheckBox>();
		GridLayout gridLayout = new GridLayout(16,16);
		gridLayout.setHgap(2);
		gridLayout.setVgap(1);
		centerPanelInmainPanel = new JPanel();
		centerPanelInmainPanel.setLayout(gridLayout);
		for(int i=0; i<256; i++)
		{
			JCheckBox  c = new JCheckBox();
			arraylistCheckBox.add(c);
			c.setSelected(false);
			centerPanelInmainPanel.add(c);
		}
		mainPanel.add(centerPanelInmainPanel,BorderLayout.CENTER);
		
		setupMidi();

		mainFrame.getContentPane().add(mainPanel);
		mainFrame.setBounds(30, 30, 600, 305);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	}
	
	public void setupMidi()
	{
		try
		{
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequence = new Sequence(Sequence.PPQ,4);
			track = sequence.createTrack();
			sequencer.setTempoInBPM(120);
		}
		catch(MidiUnavailableException exc){exc.printStackTrace();}
		catch(InvalidMidiDataException exc){exc.printStackTrace();}
	}

	
	public void buildTrackList()
	{
		int track_list[] = null;
		sequence.deleteTrack(track);
		track = sequence.createTrack();
		
		for(int i=0; i<16; i++)
		{
			track_list = new int[16];
			int key = nInstrument[i];
			for(int j=0; j<16; j++)
			{
				JCheckBox ch = (JCheckBox)arraylistCheckBox.get(j+(16*i));
				if(ch.isSelected())
					track_list[j] = key;
				else track_list[j]=0; 
			}
			makeTrack(track_list);
			track.add(makeEvent(176,1,127,0,16));
		}
		track.add(makeEvent(192,9,1,0,15));
		//track.add(makeEvent(144,1,35,100,15));
		//track.add(makeEvent(128,1,35,100,16));
		try
		{
			sequencer.setSequence(sequence);
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			sequencer.setTempoInBPM(120);
			sequencer.start();
		}	
		catch(InvalidMidiDataException exc){exc.printStackTrace();}
	}
	
	
	class StartListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			buildTrackList();
		}
	}
	
	class StopListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			sequencer.stop();
		}	
	}
	
	class TempoUpListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			float tempo = sequencer.getTempoFactor();
			System.out.println(tempo);
			sequencer.setTempoFactor((float) (tempo*1.03));
		}
	}
	
	class TempoDownListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			float tempo = sequencer.getTempoFactor();
			System.out.println(tempo);
			sequencer.setTempoFactor((float) (tempo*0.97));
		}
		
	}
	
	public void makeTrack(int track_list[])
	{
		for(int i=0; i<16; i++)
		{
			if(track_list[i]!=0)
			{
				track.add(makeEvent(144,9,track_list[i],100,i));
				track.add(makeEvent(128,9,track_list[i],100,i+1));
			}
		}
	}
	
	
	public MidiEvent makeEvent(int command, int channel, int note, int power, int tick)
	{
		MidiEvent event = null;
		try
		{
			ShortMessage message = new ShortMessage();
			message.setMessage(command, channel, note, power);
			event = new MidiEvent(message, tick);
		}
		catch(InvalidMidiDataException exc){exc.printStackTrace();}
		return event;
	}
	
	
	public static void main(String[] args)
	{
		BeatBox2 obj = new BeatBox2();
		obj.buildGUI();

	}

}
