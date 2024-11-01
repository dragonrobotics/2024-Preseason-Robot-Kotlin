package frc.robot.subsystems

import com.revrobotics.CANSparkBase
import com.revrobotics.CANSparkLowLevel
import com.revrobotics.CANSparkMax
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants

object ShooterSubsystem : SubsystemBase() {
    private val shootMotor = CANSparkMax(Constants.OperatorConstants.lowerShooterPort, CANSparkLowLevel.MotorType.kBrushless)
    private val shootMotorFollower = CANSparkMax(Constants.OperatorConstants.higherShooterPort, CANSparkLowLevel.MotorType.kBrushless)

    private val shootEncoder = shootMotor.getEncoder()

    init {
        shootMotor.restoreFactoryDefaults()
        shootMotorFollower.restoreFactoryDefaults()

        shootMotorFollower.follow(shootMotor)
        shootMotor.setIdleMode(CANSparkBase.IdleMode.kBrake)
        shootMotorFollower.setIdleMode(CANSparkBase.IdleMode.kBrake)

        shootMotor.burnFlash()
        shootMotorFollower.burnFlash()
    }

    public fun shoot(){
        shootMotor.setVoltage(12.0)
    }

    public fun stop(){
        shootMotor.stopMotor()
    }

    public fun getShooterSpeed() : Double{
        return shootEncoder.velocity
    }

}