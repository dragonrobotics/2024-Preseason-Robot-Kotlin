package frc.robot.commands

import com.pathplanner.lib.auto.AutoBuilder
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.PrintCommand
import frc.robot.subsystems.ExampleSubsystem

object Autos
{
    val autoModeChooser = AutoBuilder.buildAutoChooser()

    init{
        SmartDashboard.putData("Auto Chooser", autoModeChooser)
    }

    public fun getAutonomousCommand(): Command{
        return autoModeChooser.selected
    }

}