package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.revrobotics.CANSparkLowLevel
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants

object IntakeSubsystem: SubsystemBase() {
    private val intakeMotor = WPI_TalonSRX(Constants.OperatorConstants.lowerIntake)
    private val intakeFollower = WPI_TalonSRX(Constants.OperatorConstants.higherIntake)
    private val indexer = CANSparkMax(Constants.OperatorConstants.indexer, CANSparkLowLevel.MotorType.kBrushless)
    private val beamBreak = DigitalInput(Constants.OperatorConstants.beamBreak)

    init{
        intakeFollower.follow(intakeMotor)
        indexer.setSmartCurrentLimit(40)
    }

    override fun periodic() {
        SmartDashboard.putBoolean("BeamBreak", hasNote())
    }

    public fun Pull(){
        intakeMotor.setVoltage(8.0)
        indexer.setVoltage(-10.0)
    }

    public fun Push(){
        intakeMotor.setVoltage(-12.0)
        indexer.setVoltage(12.0)
    }

    public fun stop(){
        intakeMotor.stopMotor()
        indexer.stopMotor()
    }

    public fun hasNote(): Boolean{
        return !beamBreak.get()
    }

}