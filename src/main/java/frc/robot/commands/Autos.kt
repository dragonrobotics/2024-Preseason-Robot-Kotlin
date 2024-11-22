package frc.robot.commands

import com.pathplanner.lib.auto.AutoBuilder
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command

object Autos
{
    private val autoModeChooser: SendableChooser<Command> = AutoBuilder.buildAutoChooser()

    init{
        SmartDashboard.putData("Auto Chooser", autoModeChooser)
    }

    fun getAutonomousCommand(): Command{
        return autoModeChooser.selected
    }

}