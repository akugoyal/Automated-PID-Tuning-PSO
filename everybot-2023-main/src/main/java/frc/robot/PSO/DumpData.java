package frc.robot.PSO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

import frc.robot.RobotMap;
import frc.robot.util.Telemetry;
import frc.robot.PSO.Particle.FunctionType;

public class DumpData implements Runnable {

   private static final double DEFAULT_BEGIN_RANGE = 0; //TODO
   private static final double DEFAULT_END_RANGE = 0; //TODO

   public Particle[] particles;
   public FunctionType function = FunctionType.Error;

   public int dimensionNum = 3;
   public int numOfParticles = 20; //TODO
   


   public void run() {
        particles = new Particle[20];
      for(int i = 0; i < RobotMap.Arm.loadFiles_Log.length; i++) {

         Telemetry.putString("pivot", "loadfile", RobotMap.Arm.loadFiles_Log[i]);
         RobotMap.Arm.currentSaveFile_Log = RobotMap.Arm.saveFiles_Log[i];

         initializeFromFile(RobotMap.Arm.loadFiles_Log[i]);

         for(int j = 0; j < particles.length; j++) {
            Telemetry.putNumber("pivot", "particleNum", j);
         
            double[] position = particles[j].getPosition().getDimensions();

            Function.errorFunction(position[0], position[1], position[2]);

         }
         
      }
   }

   private void initializeFromFile(String fileToLoad) { //TODO add config checkers
      Scanner in;
      try {
          in = new Scanner(new File(fileToLoad));
      } catch (FileNotFoundException e) {
          e.printStackTrace();
          throw new RuntimeException();
      }

      try {
          in.nextDouble();
      } catch (InputMismatchException e) {
          
          e.printStackTrace();
      }


/** This part is redundant */

      double[] bPos = new double[dimensionNum];

      for(int j = 0; j < dimensionNum; j++) {
          try {
              bPos[j] = in.nextDouble();
          } catch (InputMismatchException e) {
              bPos[j] = 0.0; //TODO do smth else here
              e.printStackTrace();
          }
      }

      // bestPosition = new Vector(bPos);

      for(int i = 0; i < numOfParticles; i++) {

          double[] pos = new double[dimensionNum];

          for(int j = 0; j < dimensionNum; j++) { // current position
              try {
                  pos[j] = in.nextDouble();
              } catch (InputMismatchException e) {
                  pos[j] = Particle.rand(DEFAULT_BEGIN_RANGE, DEFAULT_END_RANGE); //TODO do smth else here
                  e.printStackTrace();
              }
          }

          Vector position = new Vector(pos);

          double[] vel = new double[dimensionNum];

          for(int j = 0; j < dimensionNum; j++) { // current velocity
              try {
                  vel[j] = in.nextDouble();
              } catch (InputMismatchException e) {
                  vel[j] = Particle.rand(DEFAULT_BEGIN_RANGE, DEFAULT_END_RANGE); //TODO do smth else here
                  e.printStackTrace();
              }
          }

          Vector velocity = new Vector(vel);

          double[] bIndivPos = new double[dimensionNum];

          for(int j = 0; j < dimensionNum; j++) { // best individual position
              try {
                  bIndivPos[j] = in.nextDouble();
              } catch (InputMismatchException e) {
                  bIndivPos[j] = 0.0; //TODO do smth else here
                  e.printStackTrace();
              }
          }

          Vector bestIndividualPosition = new Vector(bIndivPos);

          double bestIndivEval;
          try {
              bestIndivEval = in.nextDouble();
          } catch (InputMismatchException e) {
              bestIndivEval = 0.0; //TODO do smth else here
              e.printStackTrace();
          }

          particles[i] = new Particle(function, dimensionNum, position, velocity, bestIndividualPosition, bestIndivEval);

      }
  }
}
