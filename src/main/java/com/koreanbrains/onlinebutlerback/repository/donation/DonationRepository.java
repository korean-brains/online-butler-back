package com.koreanbrains.onlinebutlerback.repository.donation;

import com.koreanbrains.onlinebutlerback.entity.donation.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long> {
}
