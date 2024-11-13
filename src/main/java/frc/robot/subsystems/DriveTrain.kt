package frc.robot.subsystems

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase

import java.lang.Math.signum

import java.io.File
import java.util.Optional
import java.util.function.DoubleSupplier

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.util.HolonomicPathFollowerConfig
import com.pathplanner.lib.util.PIDConstants
import com.pathplanner.lib.util.ReplanningConfig

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation3d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.geometry.Translation3d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Filesystem
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import swervelib.parser.SwerveParser
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity
import swervelib.SwerveDrive



object DriveTrain: SubsystemBase() {
    var swerveJsonDirectory = File(Filesystem.getDeployDirectory(), "swerve")

    val maximumSpeed = 3.66;
    val maxRotationalSpeed = 3;
    var swerveDrive = SwerveParser(swerveJsonDirectory).createSwerveDrive(maximumSpeed);

    var pose = Pose2d()
    var speeds = ChassisSpeeds()

    fun DriveTrain() {
        swervelib.telemetry.SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;
        var swerveJsonDirectory = File(Filesystem.getDeployDirectory(), "swerve")
        try {
            swerveDrive = SwerveParser(swerveJsonDirectory).createSwerveDrive(maximumSpeed);
        } catch (e: Exception) {
            e.printStackTrace();
            System.exit(1);
        }
        swerveDrive.headingCorrection = true;
        zero();

        pose = Pose2d()
        speeds = ChassisSpeeds()

        AutoBuilder.configureHolonomic({swerveDrive.getPose()}, {swerveDrive.resetOdometry(pose)},
            {swerveDrive.getRobotVelocity()}, {swerveDrive.setChassisSpeeds(speeds)},
                HolonomicPathFollowerConfig(
                    PIDConstants(0.0),
                    PIDConstants(0.0),
                maximumSpeed, 2.0,
                ReplanningConfig()),{
                    var alliance = DriverStation.getAlliance();
                    if (alliance.isPresent()) {
                        alliance.get() == DriverStation.Alliance.Red
                    }
                false;
                }, this);
    }
    
    public fun getDriveCommand(translationY: DoubleSupplier, translationX: DoubleSupplier ,
    angularRotation: DoubleSupplier , fieldRelative: Boolean ): Command {
        return run({
            var xSpeed = translationX.getAsDouble();
            var ySpeed = translationY.getAsDouble();
            var rSpeed = angularRotation.getAsDouble();
            xSpeed = xSpeed * xSpeed * signum(xSpeed) * maximumSpeed;
            ySpeed = ySpeed * ySpeed * signum(ySpeed) * maximumSpeed;
            rSpeed = rSpeed * rSpeed * -signum(rSpeed) * maxRotationalSpeed;
            swerveDrive.drive(Translation2d(xSpeed, ySpeed), rSpeed, fieldRelative, false);
        });
    }

    public fun zero() {
        swerveDrive.zeroGyro();
    }
}
