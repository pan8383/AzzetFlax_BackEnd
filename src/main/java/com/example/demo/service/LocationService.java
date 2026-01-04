package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.request.LocationCreateRequestDTO;
import com.example.demo.dto.request.LocationDeleteRequestDTO;
import com.example.demo.dto.response.LocationResponseDTO;
import com.example.demo.entity.LocationEntity;
import com.example.demo.exception.ApiErrorStatus;
import com.example.demo.exception.LocationException;
import com.example.demo.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {
	private final LocationRepository locationRepository;

	/**
	 * ロケーションを取得する
	 * @return
	 */
	public List<LocationResponseDTO> getLocations() {
		return locationRepository.findAllActive()
				.stream()
				.map(LocationResponseDTO::from)
				.toList();
	}

	/**
	 * ロケーションを作成する
	 * @param request
	 * @return
	 */
	public LocationEntity create(LocationCreateRequestDTO request) {

		// 同じ名前で登録されていた場合、削除フラグを解除する
		Optional<LocationEntity> optLocation = locationRepository.findByName(request.getName());

		return optLocation.map(Location -> {
			if (Location.getIsDeleted()) {
				Location.setIsDeleted(false);
				return locationRepository.save(Location);
			} else {
				throw new LocationException(ApiErrorStatus.LOCATION_NAME_ALREADY_EXISTS);
			}
		}).orElseGet(() -> {

			// コードが重複した場合例外を発生
			if (locationRepository.existsById(request.getLocationCode())) {
				throw new LocationException(ApiErrorStatus.LOCATION_CODE_ALREADY_EXISTS);
			}

			// 新規作成
			LocationEntity newLocation = LocationEntity.builder()
					.locationCode(request.getLocationCode())
					.name(request.getName())
					.parentCode(emptyToNull(request.getParentCode()))
					.sortOrder(nullToZero(request.getSortOrder()))
					.isDeleted(false)
					.build();
			return locationRepository.save(newLocation);
		});
	}

	/**
	 * 空文字の場合は null に変換
	 */
	private String emptyToNull(String value) {
		return (value == null || value.isEmpty()) ? null : value;
	}

	/**
	 * nullの場合は 0 に変換
	 */
	private Integer nullToZero(Integer value) {
		return value != null ? value : 0;
	}

	/**
	 * ロケーションを削除する
	 * @param request
	 * @return
	 */
	public void delete(LocationDeleteRequestDTO request) {
		LocationEntity Location = locationRepository.findById(request.getLocationCode())
				.orElseThrow(() -> new LocationException(ApiErrorStatus.LOCATION_NOT_FOUND));
		Location.setIsDeleted(true);
		locationRepository.save(Location);
	}
}
