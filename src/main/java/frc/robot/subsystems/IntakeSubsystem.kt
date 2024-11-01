package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.revrobotics.CANSparkLowLevel
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants

object IntakeSubsystem: SubsystemBase() {
    private val intakeMotor = WPI_TalonSRX(Constants.OperatorConstants.lowerIntake)
    private val intakeFollower = WPI_TalonSRX(Constants.OperatorConstants.higherIntake)
    private val indexer = CANSparkMax(Constants.OperatorConstants.indexer, CANSparkLowLevel.MotorType.kBrushless)
}